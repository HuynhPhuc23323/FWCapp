package com.fwc.cosmetic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.fwc.cosmetic.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ProductDetailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    AppCompatTextView tvName, tvPrice, tvDescription, tvBrand, tvMadeIn;
    AppCompatButton btnAdd;
    AppCompatEditText edtQuantity;
    AppCompatImageView ivThumbnail, ivAdd, ivMinus;

    private Product product;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product = getIntent().getExtras().getParcelable("product");

        mAuth = FirebaseAuth.getInstance();

        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvBrand = findViewById(R.id.tvBrand);
        tvMadeIn = findViewById(R.id.tvMadeIn);
        btnAdd = findViewById(R.id.btnAdd);
        edtQuantity = findViewById(R.id.edtQuantity);
        ivThumbnail = findViewById(R.id.ivThumbnail);
        ivAdd = findViewById(R.id.ivAdd);
        ivMinus = findViewById(R.id.ivMinus);

        ivAdd.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(edtQuantity.getText().toString());
            currentQuantity++;
            if (currentQuantity < 100) {
                edtQuantity.setText(Integer.toString(currentQuantity));
            }
        });
        ivMinus.setOnClickListener(view -> {
            int currentQuantity = Integer.parseInt(edtQuantity.getText().toString());
            currentQuantity--;
            if (currentQuantity > 0) {
                edtQuantity.setText(Integer.toString(currentQuantity));
            }
        });

        btnAdd.setOnClickListener(view -> {
            Map<String, Object> cart = new HashMap<>();
            cart.put("productId", product.getId());
            cart.put("quantity", Integer.parseInt(edtQuantity.getText().toString()));
            cart.put("productName", product.getName());
            cart.put("productPrice", product.getPrice());
            cart.put("productImage", product.getImage());

            db.collection("Carts")
                    .document(mAuth.getUid())
                    .collection("Cart")
                    .document(product.getId())
                    .set(cart)
                    .addOnSuccessListener((OnSuccessListener) o -> Toast.makeText(ProductDetailActivity.this, "Add to cart success", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ProductDetailActivity.this, "Add to cart failed", Toast.LENGTH_SHORT).show());
        });

        setData();
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        tvName.setText(product.getName());
        tvDescription.setText(product.getDescription());
        tvBrand.setText("Brand: " + product.getBrand());
        tvMadeIn.setText("Made in: " + product.getMadeIn());
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        tvPrice.setText(currencyVN.format(product.getPrice()));

        Glide.with(this)
                .load(product.getImage())
                .into(ivThumbnail);
        edtQuantity.setText("1");
    }
}