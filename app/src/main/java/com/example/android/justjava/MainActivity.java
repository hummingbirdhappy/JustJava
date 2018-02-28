package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.NumberFormat;


/**
 * This app displays an order form to order coffee.
 */

public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100){
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            // Exit the method
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);

    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view){
        if (quantity == 1){
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            // Exit the method
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);

    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Gets the user input name
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String nameText = nameField.getText().toString();

        // Determines if user wants whipped cream
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Determines if user wants chocolate
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, nameText);

        // Use an intent to launch an email app.
        // Send the order summary in the email body.
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "JustJava order for " + nameText);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        // Price of 1 cup of coffee
        int basePrice = 5;
        // Add $1 if the user wants whipped cream
        if (addWhippedCream){
            basePrice = basePrice + 1;
        }
        // Add $2 if the user wants chocolate
        if (addChocolate){
            basePrice = basePrice + 2;
        }
        // Calculate the total order price by multiplying quantity and the base price
        return quantity * basePrice;
    }

    /*
    * This method creates an order summary
    *
    * @param price of order
    * @param addWhippedCream is whether or not the user wants whipped cream topping
    * @param addChocolate is whether or not the user wants chocolate topping
    * @param name is the user input name
    * @return text summary
    */
    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate,
                                      String name){
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\n" + getString(R.string.add_whipped_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.add_chocolate, addChocolate);
        priceMessage += "\n" + getString(R.string.order_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_total,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}
