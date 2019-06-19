package app.productosfirebase;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductList extends ArrayAdapter<Product> {
    private Activity context;

    List<Product> Product;

    public  ProductList(Activity context, List<Product> Product) {
        super(context, R.layout.activity_layout_product_list, Product);
        this.context = context;
        this.Product =Product;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_layout_product_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById
                (R.id.textViewName);
        TextView textviewemail = (TextView) listViewItem.findViewById
                (R.id.textviewemail);
        TextView textviewnumber = (TextView) listViewItem.findViewById
                (R.id.textviewnumber);

        Product product = Product.get(position);

        textViewName.setText(product.getName());

        textviewemail.setText(product.getDescription());

        textviewnumber.setText(product.getPrice());

        return listViewItem;
    }

}
