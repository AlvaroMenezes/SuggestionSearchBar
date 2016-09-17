package com.alvaromenezes.suggestionsearch;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String _ID = "_id";
    private final String _NAME = "_name";
    private final String _CODE = "_code";
    private final int ID = 0;
    private final int NAME = 1;
    private final int CODE = 2;


    private SearchView searchView;
    private SuggestAdapter suggestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);


        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("SEARCH HERE...");

        Cursor cursor = getCursor(getProducts());

        suggestAdapter = new SuggestAdapter(this, cursor);
        searchView.setSuggestionsAdapter(suggestAdapter);


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {

                Cursor c = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                searchView.setQuery(c.getString(1), false);
                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       // int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }



    //THE FILTER'S MAGIC ARE HERE
    private class SuggestionProvider implements FilterQueryProvider {

        private Cursor cursor;
        public SuggestionProvider(){

        }

        @Override
        public Cursor runQuery(CharSequence constraint) {
            String search = constraint.toString().toLowerCase();

            final List<Product> allProducts = getProducts();
            final List<Product> filteredProducts = new ArrayList<>();

            String name;
            String code;

            for (int i = 0; i < allProducts.size(); i++) {

                name = allProducts.get(i).name.toLowerCase();
                code = allProducts.get(i).code.toLowerCase();

                if (code.contains(search) || name.contains(search)) {
                    filteredProducts.add(allProducts.get(i));
                }
            }

            return MainActivity.this.getCursor(filteredProducts);

        }
    }


    private class SuggestAdapter extends CursorAdapter {

        public SuggestAdapter(Context context, Cursor c) {
            super(context, c, 0);
            this.setFilterQueryProvider(new SuggestionProvider());

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            View view = LayoutInflater.from(context).inflate(R.layout.search_suggestion_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.name.setText(cursor.getString(1));
            viewHolder.code.setText(cursor.getString(2));

        }

        //CUSTOM LIST ITEM LAYOUT
        public class ViewHolder {
            public final TextView name;
            public final TextView code;

            public ViewHolder(View view) {
                name = (TextView) view.findViewById(R.id.description);
                code = (TextView) view.findViewById(R.id.code);
            }
        }

    }


    /**
     * transform array of object product into MatrixCursor
     *
     * @param products
     * @return
     */
    private MatrixCursor getCursor(List<Product> products) {

        final String[] columns = new String[]{_ID, _NAME, _CODE};
        final Object[] object = new Object[]{"", "", ""};

        final MatrixCursor matrixCursor = new MatrixCursor(columns);

        for (int i = 0; i < products.size(); i++) {

            object[0] = i;
            object[1] = products.get(i).name;
            object[2] = products.get(i).code;

            matrixCursor.addRow(object);
        }

        return matrixCursor;
    }


    /**
     * MOCK DATA COULD BE SOMETHING
     *
     * @return
     */
    private List<Product> getProducts() {


        List<Product> products = new ArrayList<>();
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));
        products.add(new Product("HONDA", "CBX300"));
        products.add(new Product("HONDA", "CBX"));
        products.add(new Product("HONDA", "CG160"));
        products.add(new Product("HONDA", "CG150"));
        products.add(new Product("HONDA", "CG125"));
        products.add(new Product("HONDA", "BIZ"));
        products.add(new Product("KAWASAKI", "NINJA"));
        products.add(new Product("YAMAHA", "YBR125"));
        products.add(new Product("YAMA", "YBR125"));


        return products;
    }


}
