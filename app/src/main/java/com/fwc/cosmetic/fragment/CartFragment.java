package com.fwc.cosmetic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fwc.cosmetic.R;
import com.fwc.cosmetic.adapter.CartAdapter;
import com.fwc.cosmetic.adapter.CartClickListener;
import com.fwc.cosmetic.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartClickListener {

    private TextView tvTotalValue, tvEmpty;
    private RecyclerView rvCart;
    private final List<Cart> cartList = new ArrayList();
    private CartAdapter cartAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCart = view.findViewById(R.id.rvCart);
        tvTotalValue = view.findViewById(R.id.tvTotalValue);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        cartAdapter = new CartAdapter(getActivity(), this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvCart.setLayoutManager(mLayoutManager);

        rvCart.setAdapter(cartAdapter);

        getCarts();

        return view;
    }

    private void getCarts() {
        db.collection("Carts")
                .document(mAuth.getCurrentUser().getUid())
                .collection("Cart")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cartList.clear();
                        Long totalValue = 0L;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Cart cart = document.toObject(Cart.class);
                            cartList.add(cart);
                            totalValue += cart.getProductPrice() * cart.getQuantity();
                        }
                        if(cartList.isEmpty()){
                            tvEmpty.setVisibility(View.VISIBLE);
                            rvCart.setVisibility(View.GONE);
                        } else {
                            tvEmpty.setVisibility(View.GONE);
                            rvCart.setVisibility(View.VISIBLE);
                            cartAdapter.setData(cartList);
                        }
                        Locale localeVN = new Locale("vi", "VN");
                        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
                        tvTotalValue.setText(currencyVN.format(totalValue));
                    }
                });
    }

    @Override
    public void onRemoveClick(Cart cart) {
        db.collection("Carts")
                .document(mAuth.getCurrentUser().getUid())
                .collection("Cart")
                .document(cart.getProductId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        getCarts();
                    }
                });
    }
}