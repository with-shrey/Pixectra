package com.pixectra.app.Utils;

import com.crashlytics.android.answers.AddToCartEvent;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.InviteEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.SignUpEvent;
import com.crashlytics.android.answers.StartCheckoutEvent;
import com.pixectra.app.Models.CheckoutData;
import com.pixectra.app.Models.Product;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

/**
 * Created by swaini negi on 07/02/2018.
 */

public class LogManager {

    //time
    Date currentTime = Calendar.getInstance().getTime();



    //read previous contents of the file and then append data with the existing content.
    public static void userSignIn(boolean success, String type, String Uid) {
        Answers.getInstance().logLogin(new LoginEvent()
                .putMethod(type)
                .putSuccess(success)
                .putCustomAttribute("uid", Uid));

    }

    public static void userSignUp(boolean success, String type, String Uid) {
        Answers.getInstance().logSignUp(new SignUpEvent()
                .putMethod(type)
                .putSuccess(success)
                .putCustomAttribute("uid", Uid));
    }

    public static void inviteLinkCreated(String Uid, String url) {
        Answers.getInstance().logInvite(new InviteEvent()
                .putMethod("SMS")
                .putCustomAttribute("uid", Uid)
                .putCustomAttribute("link", url));
    }

    public static void addToCart(Product product) {
        Answers.getInstance().logAddToCart(new AddToCartEvent()
                .putItemPrice(BigDecimal.valueOf(product.getPrice()))
                .putCurrency(Currency.getInstance("INR"))
                .putItemName(product.getTitle())
                .putItemType(product.getType())
                .putItemId(product.getId()));
    }

    public static void purchaseComplete(CheckoutData checkoutData, String folder, boolean status, String id) {
        Answers.getInstance().logPurchase(new PurchaseEvent()
                .putItemPrice(BigDecimal.valueOf(checkoutData.getPrice().getTotal()))
                .putCurrency(Currency.getInstance("INR"))
                .putCustomAttribute("folder", folder)
                .putCustomAttribute("info", checkoutData.toString())
                .putCustomAttribute("paymentid", id)
                .putSuccess(status));

    }

    public static void checkOutStarted(CheckoutData data) {
        Answers.getInstance().logStartCheckout(new StartCheckoutEvent()
                .putTotalPrice(BigDecimal.valueOf(data.getPrice().getTotal()))
                .putCurrency(Currency.getInstance("INR"))
                .putItemCount(CartHolder.getInstance().getCart().size()));
    }

    public static void viewContent(String id, String name, String type) {
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(name)
                .putContentType(type)
                .putContentId(id));
    }

}
