package app.productosfirebase;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText editTextName, editTextDescripcion, editTextNumero;
    Button buttonAddProduct;
    ListView listViewProduct;
    List<Product> Product;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initListner();
    }

    private void findViews() {
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
        editTextNumero = (EditText) findViewById(R.id.editTextPrecio);
        listViewProduct= (ListView) findViewById(R.id.listViewProduct);
        buttonAddProduct = (Button) findViewById(R.id.buttonAddProduct);
        Product = new ArrayList<>();
    }

    private void initListner() {
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });

        listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = Product.get(i);
                CallUpdateAndDeleteDialog(product.getProductoId(), product.getName(), product.getDescription(),product.getPrice());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);

                    Product.add(product);
                }

                ProductList ProductAdapter = new ProductList(MainActivity.this, Product);

                listViewProduct.setAdapter(ProductAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void CallUpdateAndDeleteDialog(final String ProductId, String Name, final String Descripcion, String Precio) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.activity_update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText updateTextName= (EditText) dialogView.findViewById(R.id.updateNombre);
        final EditText updateTextDescripcion = (EditText) dialogView.findViewById(R.id.updateDescripcion);
        final EditText updateTextPrecio = (EditText) dialogView.findViewById(R.id.updatePrecio);

        updateTextName.setText(Name);
        updateTextDescripcion.setText(Descripcion);
        updateTextPrecio.setText(Precio);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);

        dialogBuilder.setTitle(Name);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateTextName.getText().toString().trim();
                String descripcion = updateTextDescripcion.getText().toString().trim();
                String precio = updateTextPrecio.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(descripcion)) {
                        if (!TextUtils.isEmpty(precio)) {
                            updateProduct(ProductId, name, descripcion, precio);
                            b.dismiss();
                        }
                    }
                }

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(ProductId);
                b.dismiss();
            }
        });
    }

    private boolean updateProduct(String id, String name, String descripcion, String precio) {
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("products").child(id);
        Product product = new Product(id, name,descripcion,precio);

        UpdateReference.setValue(product);

        Toast.makeText(getApplicationContext(), "Producto editado", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteProduct(String id) {
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("products").child(id);
        DeleteReference.removeValue();

        Toast.makeText(getApplicationContext(), "Producto Eliminado eliminado", Toast.LENGTH_LONG).show();
        return true;
    }

    private void addProduct() {
        String name = editTextName.getText().toString().trim();
        String  descripcion = editTextDescripcion.getText().toString().trim();
        String  precio = editTextNumero.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(descripcion)) {
                if (!TextUtils.isEmpty(precio)) {

                    String id = databaseReference.push().getKey();

                    Product product = new Product(id,name,descripcion,precio);

                    databaseReference.child(id).setValue(product);

                    editTextName.setText("");
                    editTextDescripcion.setText("");
                    editTextNumero.setText("");

                    Toast.makeText(this, "Producto agregado", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Ingrese un Precio", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, "Ingrese una descripcion", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_LONG).show();
        }
    }


}
