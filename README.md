# Custom Suggestion for Android Search Bar

Add a custom suggestion for native Material Design Searchview bar and filter when to use.</br></br>

Attention, this code is for help, it isn't a library! So you can take the idea and change or adapt to your need.</br>

Things that this code coud help you: </br>

  - Filter for one or more fields.
  - Create your own list item layout.
  - Fill suggestion list with your collection.(eg. Array of objects, Strings or others)

For more details donwload the project for test.I hope it help you.

###Step 1 
Add item to menu file.</br>

```xml

 <item android:id="@+id/action_search"
        android:title="Search"
        android:icon="@drawable/abc_ic_search_api_mtrl_alpha"
        app:showAsAction="ifRoom|collapseActionView"
        app:actionViewClass="android.support.v7.widget.SearchView" />
```


###Step 2
Enable Search view.</br>


```java

  @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);


        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("SEARCH HERE...");

        Cursor cursor = getCursor(getProducts());			//  <--
        suggestAdapter = new SuggestAdapter(this, cursor);	//  <---- Here the most important
        searchView.setSuggestionsAdapter(suggestAdapter);	//  <--

		//optional
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

		//optional
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

```


### Step 3 
Create adapter file.</br>

```java
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



```

### Step 4
Create Filter.</br>

```java
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

```


###Step 5 
BONUS How to create a cursor (if you use your own array).</br>

```java
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
    
```
##Developed By
Álvaro Menezes - <alvaromenezes.inf@gmail.com>



