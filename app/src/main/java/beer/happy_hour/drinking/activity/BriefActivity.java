package beer.happy_hour.drinking.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.generate_order_async.GenerateOrderAPIAsync;
import beer.happy_hour.drinking.model.DeliveryPlace;
import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.model.ShoppingCartSingleton;
import beer.happy_hour.drinking.model.User;

public class BriefActivity extends AppCompatActivity implements GenerateOrderAPIAsync.Listener {

    ProgressDialog finalProgressDialog;
    private User user;
    private DeliveryPlace deliveryPlace;
    private ShoppingCartSingleton cart;
    private TextView contact_brief_text_view;
    private TextView address_brief_text_view;
    //    private ArrayAdapter<ListItem> adapter;
//    private LinearLayout items_brief_linear_layout;
    private CheckBox majority_check_box;
    private LayoutParams popUpParams;
    private LinearLayout popUpContainerLayout;
    private PopupWindow popUpWindow;
    private Button closePopUpButton;
    private TextView popUpTextView;
    private TextView items_quantity_brief_text_view;
    private TextView total_brief_text_view;

    private String USER_NAME_PREFIX = "Nome: ";
    private String USER_PHONE_PREFIX = "Telefone: ";
    private String USER_EMAIL_PREFIX = "Email: ";

    private String DELIVERYPLACE_ADRESS_PREFIX = "Endereço: ";
    private String DELIVERYPLACE_CITYSTATE_PREFIX = "Cidade: ";
    private String DELIVERYPLACE_COUNTRY_PREFIX = "Pais: ";

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
        cart = ShoppingCartSingleton.getInstance();

        contact_brief_text_view = (TextView) findViewById(R.id.contact_brief_text_view);
        address_brief_text_view = (TextView) findViewById(R.id.address_brief_text_view);

        contact_brief_text_view.setText(""
                + USER_NAME_PREFIX + user.getName()
                + "\n" + USER_PHONE_PREFIX + user.getPhone()
                + "\n" + USER_EMAIL_PREFIX + user.getEmail()
        );

        address_brief_text_view.setText(""
                + DELIVERYPLACE_ADRESS_PREFIX + deliveryPlace.getAdress()
                + "\n" + DELIVERYPLACE_CITYSTATE_PREFIX + deliveryPlace.getCityState()
                + "\n" + DELIVERYPLACE_COUNTRY_PREFIX + deliveryPlace.getCountryName()
        );

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

//        adapter = new BriefItemsAdapter(this.getApplicationContext());
//        items_brief_linear_layout.setAdapter(adapter);


//        items_brief_linear_layout.setScrollBarSize(cart.getItemsQuantity());

        majority_check_box = (CheckBox) findViewById(R.id.majority_confirmation_checkBox);

        //Initiating popup windows
        try {
            popUpContainerLayout = new LinearLayout(this);
//            popUpMainLayout = new LinearLayout(this);
            popUpWindow = new PopupWindow(this);

            popUpTextView = new TextView(this);
            popUpTextView.setText(R.string.majority_popup_message);
            popUpTextView.setTextSize(35);

            popUpParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);


            closePopUpButton = new Button(this);
            closePopUpButton.setTextSize(35);
            closePopUpButton.setText("Ok");
            closePopUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popUpWindow.dismiss();
                }
            });

            popUpContainerLayout.setOrientation(LinearLayout.VERTICAL);
            popUpContainerLayout.addView(popUpTextView, popUpParams);
            popUpContainerLayout.addView(closePopUpButton, LayoutParams.MATCH_PARENT);
            popUpContainerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            popUpWindow.setContentView(popUpContainerLayout);
//            popUpMainLayout.addView(closePopUpButton, popUpParams);
//            setContentView(popUpMainLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finalProgressDialog = new ProgressDialog(this);
        finalProgressDialog.setMessage("Finalizando. Aguarde um momento..");
        finalProgressDialog.setIndeterminate(true);
        finalProgressDialog.setCancelable(true);
        finalProgressDialog.show();

    }

    public void generateOrder(View view) {
        if (majority_check_box.isChecked()) {
            GenerateOrderAPIAsync orderGenerator = new GenerateOrderAPIAsync();
            orderGenerator.execute();
        } else {
            popUpWindow.showAtLocation(popUpContainerLayout, Gravity.BOTTOM, 0, 0);
//            popUpWindow.update(300,300);
        }
    }


    //Código para realizar após ordem estar gerada na nossa API
    @Override
    public void onGeneratedAPIOrder() {
        //TODO: if(succesInTransaction) e (PagamentoEmCartão) -> Inicie API de pagamento
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, PaymentActivity.class));
    }
}
