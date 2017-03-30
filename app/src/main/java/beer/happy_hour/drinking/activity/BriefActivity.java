package beer.happy_hour.drinking.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.generate_order_async.orderGeneratorAPIAsync;
import beer.happy_hour.drinking.model.DeliveryPlace;
import beer.happy_hour.drinking.model.Payment;
import beer.happy_hour.drinking.model.User;
import beer.happy_hour.drinking.model.List_Item.ListItem;
import beer.happy_hour.drinking.model.List_Item.ShoppingCart;
import beer.happy_hour.drinking.repository.ListItemRepository;

public class BriefActivity extends AppCompatActivity implements orderGeneratorAPIAsync.OrderGeneratedListener {

    ProgressDialog finalProgressDialog;
    orderGeneratorAPIAsync orderGenerator;

    private User user;
    private DeliveryPlace deliveryPlace;
    private ShoppingCart cart;
    private Payment payment;
    private ListItemRepository repository;

    private TextView contact_brief_text_view;
    private TextView address_brief_text_view;
    private TextView payment_confirmation_text_view;
    //    private ArrayAdapter<ListItem> adapter;
//    private LinearLayout items_brief_linear_layout;

    private Switch majority_confirmation_switch;

    private LayoutParams pop_up_params;
    private LinearLayout pop_up_container_layout;
    private PopupWindow pop_up_window;
    private Button close_pop_up_button;
    private TextView pop_up_text_view;
    private TextView items_quantity_brief_text_view;
    private TextView total_brief_text_view;

    private LinearLayout majority_marked_container_layout;
    private LayoutParams majority_container_linear_layout_params;
    private PopupWindow majority_marked_pop_up_window;
    private TextView majority_pop_up_text_view;
    private LayoutParams layoutParamsWeightedMATCH;
    private LayoutParams layoutParamsWRAP;
    private Button majority_agree_pop_up_button;
    private Button majority_disagree_pop_up_button;
    private LinearLayout majority_buttons_linear_layout;

    private String ITEMS_QUANTITY_PREFIX = "Quantidade de produtos: ";
    private String TOTAL_PREFIX = "Total: R$ ";

    private String ITEM_QUANTITY_SUFIX = "Quantidade: ";
    private String ITEM_PRICE_SUFIX = "Preço: ";
    private String ITEM_SUBTOTAL_SUFIX = "Subtotal: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief);

        user = User.getInstance();
        deliveryPlace = DeliveryPlace.getInstance();
        cart = ShoppingCart.getInstance();
        payment = Payment.getInstance();
        repository = ListItemRepository.getInstance();

        contact_brief_text_view = (TextView) findViewById(R.id.contact_brief_text_view);
        address_brief_text_view = (TextView) findViewById(R.id.address_brief_text_view);

        contact_brief_text_view.setText(user.printBrief());

        address_brief_text_view.setText(deliveryPlace.printBrief());

//        items_brief_linear_layout = (LinearLayout) findViewById(R.id.brief_items_list_view);
//        items_brief_linear_layout.setOrientation(LinearLayout.VERTICAL);

        LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Populating ListView with brief_item for all products, each using the brief_item layout
        for (ListItem listItem : cart.getListItems()) {
            View row = inflater.inflate(R.layout.brief_item, null);

            TextView brief_item_name_text_view = (TextView) row.findViewById(R.id.brief_item_name_text_view);
            TextView brief_item_brand_text_view = (TextView) row.findViewById(R.id.brief_item_brand_text_view);
            TextView brief_item_quantity_text_view = (TextView) row.findViewById(R.id.brief_item_quantity_text_view);
            TextView brief_item_price_text_view = (TextView) row.findViewById(R.id.brief_item_price_text_view);
            TextView brief_item_subtotal_text_view = (TextView) row.findViewById(R.id.brief_item_subtotal_text_view);

            ImageView brief_item_image_view = (ImageView) row.findViewById(R.id.brief_item_image_view);

            brief_item_name_text_view.setText(listItem.getItem().getProduct().getName());
            brief_item_brand_text_view.setText(listItem.getItem().getProduct().getBrand());
            brief_item_quantity_text_view.setText(ITEM_QUANTITY_SUFIX + Double.toString(listItem.getQuantity()));
            brief_item_price_text_view.setText(ITEM_PRICE_SUFIX + Double.toString(listItem.getItem().getPrice()));
            brief_item_subtotal_text_view.setText(ITEM_SUBTOTAL_SUFIX + Double.toString(listItem.getItem().getPrice() * listItem.getQuantity()));

            //TODO: definir image view: brief_item_image_view.setImageDrawable();

//            items_brief_linear_layout.addView(row);
        }

        items_quantity_brief_text_view = (TextView) findViewById(R.id.items_quantity_brief_text_view);
        total_brief_text_view = (TextView) findViewById(R.id.total_brief_text_view);

        items_quantity_brief_text_view.setText(ITEMS_QUANTITY_PREFIX + Double.toString(cart.getItemsQuantity()));
        total_brief_text_view.setText(TOTAL_PREFIX + Double.toString(cart.getTotal()));


        payment_confirmation_text_view = (TextView) findViewById(R.id.payment_confirmation_text_view);
        payment_confirmation_text_view.setText(payment.printBrief());


//        adapter = new BriefItemsAdapter(this.getApplicationContext());
//        items_brief_linear_layout.setupAdapter(adapter);


//        items_brief_linear_layout.setScrollBarSize(cart.getItemsQuantity());


        majority_confirmation_switch = (Switch) findViewById(R.id.majority_confirmation_switch);

        majority_confirmation_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
//                    majority_marked_pop_up_window.showAtLocation(majority_marked_container_layout, Gravity.CENTER, 0, 0);
                    close_pop_up_button.setText("Estou ciente");
                    pop_up_text_view.setText(R.string.majority_confirmation_check);
                    pop_up_window.showAtLocation(pop_up_container_layout, Gravity.BOTTOM, 0, 0);
                }
                else {
                    close_pop_up_button.setText("Ok");
                    pop_up_text_view.setText(R.string.majority_popup_message);
                    pop_up_window.showAtLocation(pop_up_container_layout, Gravity.BOTTOM, 0, 0);
                }
            }
        });

        //Initiating missed majority popup window
        try {
            pop_up_container_layout = new LinearLayout(this);
//            popUpMainLayout = new LinearLayout(this);
            pop_up_window = new PopupWindow(this);

            pop_up_text_view = new TextView(this);
            pop_up_text_view.setText(R.string.majority_popup_message);
            pop_up_text_view.setTextSize(35);

            pop_up_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);


            close_pop_up_button = new Button(this);
            close_pop_up_button.setTextSize(35);
            close_pop_up_button.setText("Ok");
            close_pop_up_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pop_up_window.dismiss();
                }
            });

            pop_up_container_layout.setOrientation(LinearLayout.VERTICAL);
            pop_up_container_layout.addView(pop_up_text_view, pop_up_params);
            pop_up_container_layout.addView(close_pop_up_button, LayoutParams.MATCH_PARENT);
            pop_up_container_layout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

            pop_up_window.setContentView(pop_up_container_layout);
//            popUpMainLayout.addView(close_pop_up_button, pop_up_params);
//            setContentView(popUpMainLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        //initiate majority marked popup
//        try{
//
//            layoutParamsWRAP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//
//            layoutParamsWeightedMATCH = new LayoutParams(LayoutParams.MATCH_PARENT,
//                    LayoutParams.MATCH_PARENT);
//            layoutParamsWeightedMATCH.weight = 1;
//
//            majority_marked_container_layout = new LinearLayout(this);
////            majority_container_linear_layout_params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
////                    LayoutParams.WRAP_CONTENT);
////
////            majority_marked_container_layout.setLayoutParams(majority_container_linear_layout_params);
//
//            majority_marked_pop_up_window = new PopupWindow(this);
//
//            majority_pop_up_text_view = new TextView(this);
//            majority_pop_up_text_view.setText("Concorda que a entrega seja efetuada somente após apresentação de documento de identificação oficial com foto?");
//            majority_pop_up_text_view.setTextSize(30);
//            majority_pop_up_text_view.setLayoutParams(layoutParamsWRAP);
//
//            majority_buttons_linear_layout = new LinearLayout(this);
//            majority_buttons_linear_layout.setLayoutParams(layoutParamsWRAP);
//            majority_buttons_linear_layout.setOrientation(LinearLayout.HORIZONTAL);
//
//            majority_disagree_pop_up_button = new Button(this);
//            majority_disagree_pop_up_button.setTextSize(26);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                majority_disagree_pop_up_button.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_cancel_icon));
//            }
//            else {
//                majority_disagree_pop_up_button.setText("Discordo");
//            }
//            majority_disagree_pop_up_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //TODO:
//                    majority_marked_pop_up_window.dismiss();
//                    pop_up_window.showAtLocation(pop_up_container_layout, Gravity.BOTTOM, 0, 0);
//                }
//            });
//
//            majority_agree_pop_up_button = new Button(this);
//            majority_agree_pop_up_button.setTextSize(26);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                majority_agree_pop_up_button.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.ic_black_check));
//            }
//            else {
//                majority_agree_pop_up_button.setText("Concordo");
//            }
//            majority_agree_pop_up_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //TODO:
//                    majority_marked_pop_up_window.dismiss();
//                }
//            });
//
//
//            majority_buttons_linear_layout.addView(majority_disagree_pop_up_button, layoutParamsWeightedMATCH);
//            majority_buttons_linear_layout.addView(majority_agree_pop_up_button, layoutParamsWeightedMATCH);
//
//            majority_marked_container_layout.setOrientation(LinearLayout.VERTICAL);
//            majority_marked_container_layout.addView(majority_pop_up_text_view, LayoutParams.WRAP_CONTENT);
//            majority_marked_container_layout.addView(majority_buttons_linear_layout);
//            majority_marked_container_layout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//
//            majority_marked_pop_up_window.setContentView(majority_marked_container_layout);
//        }
//
//        catch(Exception e) {
//            e.printStackTrace();
//        }

        finalProgressDialog = new ProgressDialog(this);
        finalProgressDialog.setMessage("Finalizando. Aguarde um momento..");
        finalProgressDialog.setIndeterminate(true);
        finalProgressDialog.setCancelable(false);
    }

    public void generateOrder(View view) {
        if (majority_confirmation_switch.isChecked()) {
            orderGenerator = new orderGeneratorAPIAsync();
            orderGenerator.setListener(this);
            orderGenerator.execute();
            finalProgressDialog.show();
        } else {
            pop_up_window.showAtLocation(pop_up_container_layout, Gravity.BOTTOM, 0, 0);
//            pop_up_window.update(300,300);
        }
    }

    private void resetItems(){
        for(ListItem item : repository.getList())
            item.setQuantityAndUpdateCart(0);
    }

    //Código para realizar após ordem estar gerada na nossa API
    @Override
    public void onOrderSucceeded() {
        resetItems();

        finalProgressDialog.dismiss();
        startActivity(new Intent(this, OnOrderAcceptedActivity.class));

        //TODO: zerar todos os items e limpar carrinho
    }

    //TODO: implementar
    @Override
    public void onOrderFailed() {
        finalProgressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, PaymentActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                return (true);
            case R.id.cart_menu_item:
                startActivity(new Intent(this, CartActivity.class));
                return (true);
            case R.id.about:
                Toast.makeText(this, "About Toast!", Toast.LENGTH_LONG).show();
                return (true);
            case R.id.exit:
                finish();
                return (true);

        }
        return (super.onOptionsItemSelected(item));
    }
}
