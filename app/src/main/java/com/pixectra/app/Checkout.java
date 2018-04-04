package com.pixectra.app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Adapter.CartImagesAdapter;
import com.pixectra.app.Models.Address;
import com.pixectra.app.Models.Coupon;
import com.pixectra.app.Models.Price;
import com.pixectra.app.Models.Product;
import com.pixectra.app.Models.Tax;
import com.pixectra.app.Models.User;
import com.pixectra.app.Utils.CartHolder;
import com.pixectra.app.Utils.ImageController;
import com.pixectra.app.Utils.QReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.branch.referral.Branch;

public class Checkout extends AppCompatActivity {
    CardView empty;
    RecyclerView recyclerView;
    CartAdapter adapter;
    Address address;
    Coupon coupon;
    boolean couponApplied = false;
    SimpleDateFormat format;
    EditText couponBottomSheet;
    TextView addressText;
    ImageController uploader;
    SparseIntArray prices;
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    Tax tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Branch.getInstance().loadRewards();
        setContentView(R.layout.activity_checkout);
        uploader = new ImageController(Checkout.this, getWindow());
        couponApplied = CartHolder.getInstance().getDiscount() != null;
        coupon = CartHolder.getInstance().getCoupon();
        recyclerView = findViewById(R.id.cart_recyclerview);
        empty = findViewById(R.id.empty_view_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter();
        format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        recyclerView.setAdapter(adapter);
        findViewById(R.id.cart_proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onplaceorder();
            }
        });
        toggleVisibility();
    }

    void toggleVisibility() {
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                if (CartHolder.getInstance().getCart().size() > 0) {
                    findViewById(R.id.cart_proceed).setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                } else {
                    findViewById(R.id.cart_proceed).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChanged() {
                super.onChanged();
                if (CartHolder.getInstance().getCart().size() > 0) {
                    findViewById(R.id.cart_proceed).setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                } else {
                    findViewById(R.id.cart_proceed).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    int getBaseAmount() {
        int total = 0;
        int n = prices.size();
        for (int i = 0; i < n; i++) {
            total += prices.valueAt(i);
        }
        return total;
    }

    public void onplaceorder() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Checkout.this
                , R.style.CustomDialogTheme
        );
        View parentview = getLayoutInflater().inflate(R.layout.bottomsheet, null);
        bottomSheetDialog.setContentView(parentview);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) parentview.getParent());
        bottomSheetBehavior.setPeekHeight(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics()));
        bottomSheetDialog.show();
        couponBottomSheet = parentview.findViewById(R.id.couponedittext);
        final Button apply = parentview.findViewById(R.id.applycoupn);
        final TextView carttotal = parentview.findViewById(R.id.carttotal);
        final TextView cartDiscount = parentview.findViewById(R.id.cartdiscount);
        final TextView totalpayable = parentview.findViewById(R.id.totalpayable);
        final CheckBox credits = parentview.findViewById(R.id.credits);
        addressText = parentview.findViewById(R.id.bottom_sheet_address);

        final TextView discountType = parentview.findViewById(R.id.discount_type);
        final TextView creditsUsed = parentview.findViewById(R.id.credits_used);
        creditsUsed.setText("0.0");
        credits.setText("Use Credit Balance( Available : " + Branch.getInstance().getCredits() + ")");
        final int creditBalance = Branch.getInstance().getCredits();
        credits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    computeAndSetCreditPayment(creditsUsed, carttotal, cartDiscount, creditBalance);
                else
                    creditsUsed.setText("0");

                computeTotal(discountType, totalpayable, carttotal, cartDiscount, creditsUsed);
            }


        });
        Button cancel = parentview.findViewById(R.id.bottomsheet_cancel);
        Button pay = parentview.findViewById(R.id.bottomsheet_pay);
        Button scan = parentview.findViewById(R.id.scanqr);
        Button selectShipping = parentview.findViewById(R.id.shipping_address_choose);
        carttotal.setText(String.valueOf(getBaseAmount()));

        computeTotal(discountType, totalpayable, carttotal, cartDiscount, creditsUsed);

        selectShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Checkout.this, SelectAddressActivity.class);
                intent.putExtra("select", true);
                startActivityForResult(intent, 2);
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!couponApplied) {
                    final DatabaseReference used = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Used");
                    used.keepSynced(true);
                    final DatabaseReference earned = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Earned");
                    earned.keepSynced(true);
                    used.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String code = couponBottomSheet.getText().toString();
                            if (dataSnapshot.hasChild(code)) {
                                Toast.makeText(Checkout.this, "Offer Already Used", Toast.LENGTH_SHORT).show();
                            } else {
                                earned.child(code).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            coupon = dataSnapshot.getValue(Coupon.class);
                                            if (coupon.isCurrent()) {
                                                try {
                                                    if (format.parse(coupon.getStartDate()).compareTo(format.parse(format.format(new Date()))) <= 0
                                                            && format.parse(coupon.getEndDate()).compareTo(format.parse(format.format(new Date()))) >= 0) {
                                                        used.child(code).setValue(coupon).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                if (coupon.getType() == 0 && coupon.getThreshold() <= Double.parseDouble(carttotal.getText().toString())) {
                                                                    discountType.setText(coupon.getDiscount() + "%");
                                                                    cartDiscount.setText(String.valueOf(
                                                                            -(1.0 * coupon.getDiscount() / 100.0 * Double.parseDouble(carttotal.getText().toString()))));
                                                                } else {
                                                                    if (coupon.getThreshold() <= Double.parseDouble(carttotal.getText().toString())) {
                                                                        discountType.setText("-Rs." + coupon.getDiscount());
                                                                        cartDiscount.setText(String.valueOf(
                                                                                -(1.0 * coupon.getDiscount())));
                                                                    }
                                                                }
                                                                CartHolder.getInstance().setCoupon(coupon);
                                                                CartHolder.getInstance().setDiscount(
                                                                        new Pair<>(coupon.getType()
                                                                                , new Pair<>(coupon.getThreshold(), coupon.getDiscount())));
                                                                computeTotal(discountType, totalpayable, carttotal, cartDiscount, creditsUsed);
                                                                computeAndSetCreditPayment(creditsUsed, carttotal, cartDiscount, creditBalance);
                                                                couponApplied = true;
                                                                earned.child(code).setValue(null);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(Checkout.this, "Error Occured Try Again", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } else {
                                                        if (format.parse(coupon.getStartDate()).compareTo(format.parse(format.format(new Date()))) > 0) {
                                                            Toast.makeText(Checkout.this, "Offer Not Started Yet \n" + format.parse(coupon.getStartDate()), Toast.LENGTH_SHORT).show();
                                                        } else if (format.parse(coupon.getEndDate()).compareTo(format.parse(format.format(new Date()))) < 0) {
                                                            Toast.makeText(Checkout.this, "Offer Has Ended", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                Toast.makeText(Checkout.this, "You Will get Discount On Your Next Order Using Same Code", Toast.LENGTH_SHORT).show();
                                                earned.child(code).child("current").setValue(true);
                                                couponApplied = true;
                                                discountType.setText("-Rs." + 0);
                                                cartDiscount.setText(String.valueOf(0));
                                                CartHolder.getInstance().setDiscount(
                                                        new Pair<>(1
                                                                , new Pair<>(0, 0.0)));
                                                computeTotal(discountType, totalpayable, carttotal, cartDiscount, creditsUsed);

                                            }
                                        } else {
                                            Toast.makeText(Checkout.this, "Coupon Code Not Found", Toast.LENGTH_SHORT).show();
                                        }
                                        earned.child(code).removeEventListener(this);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        databaseError.toException().printStackTrace();
                                    }
                                });

                            }
                            used.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            databaseError.toException().printStackTrace();
                        }
                    });
                } else {
                    Toast.makeText(Checkout.this, "Only One Coupon Allowed For A Order", Toast.LENGTH_SHORT).show();
                }
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                        ContextCompat.checkSelfPermission(Checkout.this,
                                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(Checkout.this,
                            new String[]{android.Manifest.permission.CAMERA},
                            1);


                } else {
                    Intent intent = new Intent(Checkout.this, QReader.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final com.pixectra.app.Models.CheckoutData checkout = new com.pixectra.app.Models.CheckoutData();

                final DatabaseReference user = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                user.keepSynced(true);
                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        checkout.setUser(dataSnapshot.getValue(User.class));
                        Price price = new Price(Double.parseDouble(carttotal.getText().toString())
                                , Double.parseDouble(cartDiscount.getText().toString())
                                , Integer.parseInt(creditsUsed.getText().toString())
                                , Double.parseDouble(totalpayable.getText().toString())
                        );
                        checkout.setPrice(price);
                        checkout.setAddress(address);

                        CartHolder.getInstance().setCheckout(checkout);
                        user.removeEventListener(this);

                        Intent intent = new Intent(Checkout.this, PayUMoneyActivity.class);
                        intent.putExtra("name", checkout.getUser().getName());
                        intent.putExtra("email", checkout.getUser().getEmail());
                        intent.putExtra("amount", Double.parseDouble(totalpayable.getText().toString()));
                        intent.putExtra("phone", checkout.getUser().getPhoneNo());
                        intent.putExtra("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        intent.putExtra("isOneTime", true);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }


    double calculateCreditDiscount(int credits, double total) {
        if (credits > total) {
            return -1 * total;
        } else {
            return -1 * credits;
        }

    }

    private void computeAndSetCreditPayment(TextView creditsUsed, TextView carttotal, TextView cartDiscount, int creditBalance) {
        creditsUsed.setText(""
                + calculateCreditDiscount(creditBalance
                , Double.parseDouble(carttotal.getText().toString())
                        - Double.parseDouble(cartDiscount.getText().toString())));
    }

    void computeTotal(TextView discountType, TextView totalPayable, TextView carttotal, TextView cartDiscount, TextView creditsUsed) {
        if (coupon != null) {
            if (coupon.getType() == 0 && coupon.getThreshold() <= Double.parseDouble(carttotal.getText().toString())) {
                discountType.setText(coupon.getDiscount() + "%");
                cartDiscount.setText(String.valueOf(
                        -(1.0 * coupon.getDiscount() / 100.0 * Double.parseDouble(carttotal.getText().toString()))));
            } else {
                if (coupon.getThreshold() <= Double.parseDouble(carttotal.getText().toString())) {
                    discountType.setText("-Rs." + coupon.getDiscount());
                    cartDiscount.setText(String.valueOf(
                            -(1.0 * coupon.getDiscount())));
                }
            }
        }
        totalPayable.setText(String.valueOf(
                +Double.parseDouble(carttotal.getText().toString())
                        + Double.parseDouble(cartDiscount.getText().toString())
                        + Double.parseDouble(creditsUsed.getText().toString())
        ));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (couponBottomSheet != null) {
                    couponBottomSheet.setText(data.getStringExtra("code"));
                }
            } else if (requestCode == 2) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    address = (Address) bundle.getSerializable("address");
                    if (addressText != null)
                        addressText.setText(address.getName() + "\n" + address.getStreet() + "\n" + address.getMobile());
                }
            }
        } else {
            if (requestCode == 1) {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Checkout.this, QReader.class);
                startActivityForResult(intent, 1);
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            }
        }
    }


    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
        // ArrayList<String> keys;

        public CartAdapter() {
            prices = new SparseIntArray();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            return new CartAdapter.VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            Product product = CartHolder.getInstance().getCart().get(position).first;
            holder.title.setText(String.format(Locale.getDefault(), "%s ( %d )", product.getType(), CartHolder.getInstance().getCart().get(position).second.size()));
            holder.type.setText(String.format(Locale.getDefault(), "%s@%d", product.getTitle(), product.getPrice()));
            Glide.with(Checkout.this).load(product.getUrl()).into(holder.titleImage);
            holder.images.setAdapter(new CartImagesAdapter(this, Checkout.this, CartHolder.getInstance().getCart().get(position).second, position));
            int price = product.getPrice();
            String priceText = "" + price;
            if (product.getMaxPics() == -1) {
                price += (CartHolder.getInstance().getCart().get(position).second.size() - product.getMinPics()) * product.getPricePerPiece();
                priceText = priceText + "+" + (CartHolder.getInstance().getCart().get(position).second.size() - product.getMinPics()) + " X "
                        + product.getPricePerPiece();
            }
            priceText = priceText + " = " + price;
            holder.price.setText(priceText);
            prices.put(position, price);
        }

        @Override
        public int getItemCount() {
            return CartHolder.getInstance().getCart().size();
        }

        class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title, type, price;
            ImageView titleImage;
            RecyclerView images;
            ImageView remove;

            public VH(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                type = itemView.findViewById(R.id.type_price);
                titleImage = itemView.findViewById(R.id.title_image);
                images = itemView.findViewById(R.id.images_cart);
                remove = itemView.findViewById(R.id.remove);
                price = itemView.findViewById(R.id.price_of_item);
                remove.setOnClickListener(this);
                images.setLayoutManager(new LinearLayoutManager(Checkout.this, LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onClick(View view) {
                CartHolder.getInstance().getCart().remove(getAdapterPosition());
                prices.delete(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());

            }
        }
    }
}
