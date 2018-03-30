package com.pixectra.app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.pixectra.app.Models.CheckoutData;
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
import java.util.Vector;

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

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Branch.getInstance().loadRewards();
        setContentView(R.layout.activity_checkout);
        uploader = new ImageController(Checkout.this, getWindow());
        couponApplied = CartHolder.getInstance().getDiscount() != null;
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

    int getBaseAmount() {
        int total = 0;
        for (Pair<Product, Vector<Bitmap>> product : CartHolder.getInstance().getCart()) {
            total += product.first.getPrice();
        }
        return total;
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
        final TextView stategst = parentview.findViewById(R.id.stategst);
        final TextView centralgst = parentview.findViewById(R.id.centralgst);
        final TextView cess = parentview.findViewById(R.id.cess);
        final TextView totalpayable = parentview.findViewById(R.id.totalpayable);
        final CheckBox credits = parentview.findViewById(R.id.credits);
        addressText = parentview.findViewById(R.id.bottom_sheet_address);

        final TextView discountType = parentview.findViewById(R.id.discount_type);
        final TextView cgstType = parentview.findViewById(R.id.cgst_type);
        final TextView sgstType = parentview.findViewById(R.id.sgst_type);
        final TextView cessType = parentview.findViewById(R.id.cess_type);
        credits.setText("Use Credit Balance( Available : " + Branch.getInstance().getCredits() + ")");
        int creditBalance = Branch.getInstance().getCredits();
        credits.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        Button cancel = parentview.findViewById(R.id.bottomsheet_cancel);
        Button pay = parentview.findViewById(R.id.bottomsheet_pay);
        Button scan = parentview.findViewById(R.id.scanqr);
        Button selectShipping = parentview.findViewById(R.id.shipping_address_choose);
        carttotal.setText(String.valueOf(getBaseAmount()));
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Tax");
        db.keepSynced(true);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tax tax = dataSnapshot.getValue(Tax.class);
                cgstType.setText(tax.getCgst() + "%");
                sgstType.setText(tax.getSgst() + "%");
                cessType.setText(tax.getCess() + "%");

                if (tax.getType() == 0 && tax.getThreshold() <= Double.parseDouble(carttotal.getText().toString())) {
                    discountType.setText("-" + tax.getDiscount() + "%");
                    cartDiscount.setText(String.valueOf(-1.0 * tax.getDiscount() * Double.parseDouble(carttotal.getText().toString()) / 100.0));
                } else {
                    if (tax.getThreshold() <= Double.parseDouble(carttotal.getText().toString())) {
                        discountType.setText("-Rs." + tax.getDiscount());
                        cartDiscount.setText(String.valueOf(-1.0 * tax.getDiscount()));
                    }
                }
                centralgst.setText(String.valueOf(tax.getCgst() * Double.parseDouble(carttotal.getText().toString()) / 100.0));
                stategst.setText(String.valueOf(tax.getSgst() * Double.parseDouble(carttotal.getText().toString()) / 100.0));
                cess.setText(String.valueOf(tax.getCess() * Double.parseDouble(carttotal.getText().toString()) / 100.0));
                totalpayable.setText(String.valueOf(
                        Double.parseDouble(centralgst.getText().toString())
                                + Double.parseDouble(stategst.getText().toString())
                                + Double.parseDouble(cess.getText().toString())
                                + Double.parseDouble(carttotal.getText().toString())
                                + Double.parseDouble(cartDiscount.getText().toString())
                ));
                db.removeEventListener(this);
                if (CartHolder.getInstance().getDiscount() != null) {
                    if (CartHolder.getInstance().getDiscount().first == 0 && CartHolder.getInstance().getDiscount().second.first <= Double.parseDouble(carttotal.getText().toString())) {
                        discountType.setText("-" + CartHolder.getInstance().getDiscount().second.second + "%");
                        cartDiscount.setText(String.valueOf(-1.0 * CartHolder.getInstance().getDiscount().second.second * Double.parseDouble(carttotal.getText().toString()) / 100.0));
                    } else {
                        if (CartHolder.getInstance().getDiscount().second.first <= Double.parseDouble(carttotal.getText().toString())) {
                            discountType.setText("-Rs." + CartHolder.getInstance().getDiscount().second.second);
                            cartDiscount.setText(String.valueOf(-1.0 * CartHolder.getInstance().getDiscount().second.second));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                                                                CartHolder.getInstance().setDiscount(
                                                                        new Pair<>(coupon.getType()
                                                                                , new Pair<>(coupon.getThreshold(), coupon.getDiscount())));
                                                                totalpayable.setText(String.valueOf(
                                                                        Double.parseDouble(centralgst.getText().toString())
                                                                                + Double.parseDouble(stategst.getText().toString())
                                                                                + Double.parseDouble(cess.getText().toString())
                                                                                + Double.parseDouble(carttotal.getText().toString())
                                                                                + Double.parseDouble(cartDiscount.getText().toString())
                                                                ));
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
                                                totalpayable.setText(String.valueOf(
                                                        Double.parseDouble(centralgst.getText().toString())
                                                                + Double.parseDouble(stategst.getText().toString())
                                                                + Double.parseDouble(cess.getText().toString())
                                                                + Double.parseDouble(carttotal.getText().toString())
                                                                + Double.parseDouble(cartDiscount.getText().toString())
                                                ));

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
                final CheckoutData checkout = new CheckoutData();

                final DatabaseReference user = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                user.keepSynced(true);
                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        checkout.setUser(dataSnapshot.getValue(User.class));
                        Price price = new Price(Double.parseDouble(carttotal.getText().toString())
                                , Double.parseDouble(centralgst.getText().toString())
                                , Double.parseDouble(stategst.getText().toString())
                                , Double.parseDouble(cess.getText().toString())
                                , Double.parseDouble(cartDiscount.getText().toString())
                                , Double.parseDouble(totalpayable.getText().toString())
                        );
                        checkout.setPrice(price);
                        checkout.setAddress(address);




                        Intent intent = new Intent(Checkout.this, PayUMoneyActivity.class);
                        intent.putExtra("name", checkout.getUser().getName());
                        intent.putExtra("email", checkout.getUser().getEmail());
                        intent.putExtra("amount", checkout.getPrice().getTotal());
                        intent.putExtra("phone", checkout.getUser().getPhoneNo());
                        intent.putExtra("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        intent.putExtra("isOneTime", true);
                        intent.putExtra("Obj", checkout);
                        startActivity(intent);

                        user.removeEventListener(this);
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

    int calculateCreditDiscount(int credits, int total) {
        if (credits > total) {
            return total;
        } else {
            return credits;
        }

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


    class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {
        // ArrayList<String> keys;

        public CartAdapter() {
            //  keys=new ArrayList<>(CartHolder.getInstance().getCart().keySet());
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
            holder.images.setAdapter(new CartImagesAdapter(Checkout.this, CartHolder.getInstance().getCart().get(position).second, position));
        }

        @Override
        public int getItemCount() {
            return CartHolder.getInstance().getCart().size();
        }

        class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title, type;
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
                remove.setOnClickListener(this);
                images.setLayoutManager(new LinearLayoutManager(Checkout.this, LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onClick(View view) {
                CartHolder.getInstance().getCart().remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());

            }
        }
    }
}
