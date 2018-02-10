package com.pixectra.app.Utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixectra.app.Models.Product;
import com.pixectra.app.R;

public class AddCommonData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_common_data);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("CommonData");
        ref.setValue(null);
        ref.child("FlipBook").push().setValue(new Product(1,"Size 1","https://nubuntu.org/wp-content/uploads/2015/06/flipbook-12.gif",1,50,"50/Book"));
        ref.child("FlipBook").push().setValue(new Product(2,"Size 2","https://nubuntu.org/wp-content/uploads/2015/06/flipbook-12.gif",2,100,"100/Book"));
        ref.child("FlipBook").push().setValue(new Product(3,"Size 3","https://nubuntu.org/wp-content/uploads/2015/06/flipbook-12.gif",3,150,"150/Book"));
        ref.child("PostCard").push().setValue(new Product(4,"Photo Postcard","http://goulddiary.stanford.edu/gallery/images/postcard-b.jpg",1,100,"100/Card"));;
        ref.child("PostCard").push().setValue(new Product(5,"Photo With Text Postcard","http://goulddiary.stanford.edu/gallery/images/postcard-b.jpg",1,120,"120/Card"));;
        ref.child("Polaroids").push().setValue(new Product(6,"Size 1","https://i2.wp.com/yongjustin.com/operationoverhaul/wp-content/uploads/2012/08/Operation-Overhaul-Washi-Tape-Polaroids.jpg",1,50,"50/Piece"));;
        ref.child("Polaroids").push().setValue(new Product(7,"Size 2","https://i2.wp.com/yongjustin.com/operationoverhaul/wp-content/uploads/2012/08/Operation-Overhaul-Washi-Tape-Polaroids.jpg",2,100,"50/Piece"));;
        ref.child("Photos").push().setValue(new Product(8,"6 X 4","https://static.idphoto4you.com/Images/SamplePage/Indian_PAN_card_photo_sizes.png",1,50,"50/Book"));;
        ref.child("Photos").push().setValue(new Product(9,"5 X 7","https://static.idphoto4you.com/Images/SamplePage/Indian_PAN_card_photo_sizes.png",1,50,"50/Book"));;
        ref.child("Posters").push().setValue(new Product(10,"A3 Size","https://0.s3.envato.com/files/97225182/previews/metropolis-event-poster-landscape-A.jpg",1,50,"50/Book"));;
        ref.child("Posters").push().setValue(new Product(11,"A4 Size","https://0.s3.envato.com/files/97225182/previews/metropolis-event-poster-landscape-A.jpg",1,50,"50/Book"));;
        ref.child("PhotoBooks").push().setValue(new Product(12,"Lorem Ipsum","https://nubuntu.org/wp-content/uploads/2015/06/flipbook-12.gif",5,50,"50/Book"));
        ref.child("PhotoBooks").push().setValue(new Product(13,"Lorem Ipsum","https://nubuntu.org/wp-content/uploads/2015/06/flipbook-12.gif",10,200,"50/Book"));
        ref.child("PhotoBooks").push().setValue(new Product(14,"Lorem Ipsum","https://nubuntu.org/wp-content/uploads/2015/06/flipbook-12.gif",15,400,"50/Book"));

    }
}
