package com.treadingPlatformApplication.service.implement;
import com.treadingPlatformApplication.models.PaymentDetails;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.repositories.PaymentDetailsRepository;
import com.treadingPlatformApplication.service.PaymentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsServiceImple implements PaymentDetailsService {

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName, User user) {

        PaymentDetails paymentDetails = new PaymentDetails();

        paymentDetails.setBankName(bankName);
        paymentDetails.setIfsc(ifsc);
        paymentDetails.setAccountHolderName(accountHolderName);
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setUser(user);

        return this.paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return this.paymentDetailsRepository.findByUserId(user.getId());
    }
}
