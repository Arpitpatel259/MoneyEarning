package com.example.earn_money.Utils;

import com.google.android.gms.wallet.WalletConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Constants {
    public static final String Banker_Name = " ";
    public static final String Banker_UPI_ID = " ";
    public static final String Email = "email";
    public static final String KEY_MONEY = "0";
    public static final String KEY_SPIN_100 = "100_Spin";
    public static final String KEY_SPIN_20 = "20_Spin";
    public static final String KEY_SPIN_50 = "50_Spin";
    public static final String KEY_SPIN_500 = "500_Spin";
    public static final String KEY_USERNAME = "user";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;

    /**
     * The allowed networks to be requested from the API. If the user has cards from networks not
     * specified here in their account, these will not be offered for them to choose in the popup.
     *
     * @value #SUPPORTED_NETWORKS
     */
    public static final List<String> SUPPORTED_NETWORKS = Arrays.asList(
            "AMEX",
            "DISCOVER",
            "JCB",
            "MASTERCARD",
            "VISA");

    /**
     * The Google Pay API may return cards on file on Google.com (PAN_ONLY) and/or a device token on
     * an Android device authenticated with a 3-D Secure cryptogram (CRYPTOGRAM_3DS).
     *
     * @value #SUPPORTED_METHODS
     */
    public static final List<String> SUPPORTED_METHODS = Arrays.asList(
            "PAN_ONLY",
            "CRYPTOGRAM_3DS");

    /**
     * Required by the API, but not visible to the user.
     *
     * @value #COUNTRY_CODE Your local country
     */
    public static final String COUNTRY_CODE = "IND";

    /**
     * Required by the API, but not visible to the user.
     *
     * @value #CURRENCY_CODE Your local currency
     */
    public static final String CURRENCY_CODE = "INR";

    /**
     * Supported countries for shipping (use ISO 3166-1 alpha-2 country codes). Relevant only when
     * requesting a shipping address.
     *
     * @value #SHIPPING_SUPPORTED_COUNTRIES
     */
    public static final List<String> SHIPPING_SUPPORTED_COUNTRIES = Arrays.asList("US", "GB","IND");

    /**
     * The name of your payment processor/gateway. Please refer to their documentation for more
     * information.
     *
     * @value #PAYMENT_GATEWAY_TOKENIZATION_NAME
     */
    public static final String PAYMENT_GATEWAY_TOKENIZATION_NAME = "example";

    /**
     * Custom parameters required by the processor/gateway.
     * In many cases, your processor / gateway will only require a gatewayMerchantId.
     * Please refer to your processor's documentation for more information. The number of parameters
     * required and their names vary depending on the processor.
     *
     * @value #PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS
     */
    public static final HashMap<String, String> PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS =
            new HashMap<String, String>() {{
                put("gateway", PAYMENT_GATEWAY_TOKENIZATION_NAME);
                put("gatewayMerchantId", "exampleGatewayMerchantId");
                // Your processor may require additional parameters.
            }};

    /**
     * Only used for {@code DIRECT} tokenization. Can be removed when using {@code PAYMENT_GATEWAY}
     * tokenization.
     *
     * @value #DIRECT_TOKENIZATION_PUBLIC_KEY
     */
    public static final String DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME";

    /**
     * Parameters required for {@code DIRECT} tokenization.
     * Only used for {@code DIRECT} tokenization. Can be removed when using {@code PAYMENT_GATEWAY}
     * tokenization.
     *
     * @value #DIRECT_TOKENIZATION_PARAMETERS
     */
    public static final HashMap<String, String> DIRECT_TOKENIZATION_PARAMETERS =
            new HashMap<String, String>() {{
                put("protocolVersion", "ECv2");
                put("publicKey", DIRECT_TOKENIZATION_PUBLIC_KEY);
            }};
}
