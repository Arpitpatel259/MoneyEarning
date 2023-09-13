package com.kc.earn_money.Utils;

import com.google.android.gms.wallet.WalletConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Constants {
    public static final ArrayList<Integer> Spin_set_20 = new ArrayList<>();
    public static final ArrayList<Integer> Spin_set_50 = new ArrayList<>();
    public static final ArrayList<Integer> Spin_set_100 = new ArrayList<>();
    public static final ArrayList<Integer> Spin_set_500 = new ArrayList<>();
    public static final String Email = "email";
    public static final String NAME = "username";
    public static final String LoginData = "loginWith";
    public static final String KEY_MONEY = "0";
    public static String MERCHANT_ID = "";
    public static final String KEY_SPIN_100 = "100_Spin";
    public static final String KEY_SPIN_20 = "20_Spin";
    public static final String KEY_SPIN_50 = "50_Spin";
    public static final String KEY_SPIN_500 = "500_Spin";
    public static final String KEY_SPIN_LAST_100 = "value100";
    public static final String KEY_SPIN_LAST_20 = "value20";
    public static final String KEY_SPIN_LAST_50 = "value50";
    public static final String KEY_SPIN_LAST_500 = "value500";
    public static final String KEY_USERNAME = "user";
    public static final String MyPREFERENCES = "MyPrefs";
    /**
     * Google Pay Utils
     */
    public static final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_PRODUCTION;

    public static final List<String> SUPPORTED_NETWORKS = Arrays.asList("AMEX", "DISCOVER", "JCB", "MASTERCARD", "VISA");

    public static final List<String> SUPPORTED_METHODS = Arrays.asList("PAN_ONLY", "CRYPTOGRAM_3DS");

    public static final String COUNTRY_CODE = "US";

    public static final String CURRENCY_CODE = "USD";

    public static final List<String> SHIPPING_SUPPORTED_COUNTRIES = Arrays.asList("US", "GB");

    public static final String PAYMENT_GATEWAY_TOKENIZATION_NAME = "example";

    public static final HashMap<String, String> PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS = new HashMap<String, String>() {{
        put("gateway", PAYMENT_GATEWAY_TOKENIZATION_NAME);
        put("gatewayMerchantId", "vyapar.168366070686@hdfcbank");
        // Your processor may require additional parameters.
    }};

    public static final String DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME";


    public static final HashMap<String, String> DIRECT_TOKENIZATION_PARAMETERS = new HashMap<String, String>() {{
        put("protocolVersion", "ECv2");
        put("publicKey", DIRECT_TOKENIZATION_PUBLIC_KEY);
    }};
}
