package use_case.start.id_verification;
import com.plaid.client.ApiClient;
import com.plaid.client.request.PlaidApi;

public class VerificationInteractor {
    private ApiClient plaidClient; 
    public void VerificationInteractor() {
        String apiKey = System.getenv("PLAID_API");
        plaidClient = new ApiClient().setApiKey(apiKey); 

    }
}
