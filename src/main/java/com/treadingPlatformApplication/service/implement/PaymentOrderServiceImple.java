package com.treadingPlatformApplication.service.implement;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.treadingPlatformApplication.domain.PaymentMethod;
import com.treadingPlatformApplication.domain.PaymentOrderStatus;
import com.treadingPlatformApplication.exception.ResourceNotFoundException;
import com.treadingPlatformApplication.models.PaymentOrder;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.repositories.PaymentOrderRepository;
import com.treadingPlatformApplication.responce.PaymentResponce;
import com.treadingPlatformApplication.service.PaymentOrderService;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentOrderServiceImple implements PaymentOrderService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecretKey;


    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);

        return this.paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) {

        return this.paymentOrderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("PaymentOrder","id",id+""));
    }

    @Override
    public Boolean proccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {

        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){

            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecretKey);
                Payment payment = razorpayClient.payments.fetch(paymentId);

                Integer amount = payment.get("amount");
                String status = payment.get("status");


                if(status.equals("captured")){
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return  true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return  false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);

            return  true;
        }
        return false;
    }

    @Override
    public PaymentResponce createRazorpayPaymentLink(User user, Long amount) throws RazorpayException {
        Long Amount = amount*100;

        try{
            RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecretKey);
            JSONObject paymentLinkRequest  = new JSONObject();
            paymentLinkRequest.put("amount",amount);
            paymentLinkRequest.put("currency","INR");


            JSONObject customer  = new JSONObject();
            customer.put("name",user.getFullName());

            customer.put("email",user.getEmail());

            paymentLinkRequest.put("customer",customer);

            //create json object with the notification settings
            JSONObject notify = new JSONObject();
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);

            //set the reminder setting
            paymentLinkRequest.put("reminder_enable",true);

            paymentLinkRequest.put("callback_url","https://localhost:8080/wallet");

            paymentLinkRequest.put("callback_method","get");

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String PaymentLinkUrl = payment.get("short_url");

            PaymentResponce paymentResponce = new PaymentResponce();

            paymentResponce.setPaymentUrl(PaymentLinkUrl);
            return  paymentResponce;
        }catch (RazorpayException e){
            System.out.println("Error creating paymeny link"+e.getMessage());
            throw  new RazorpayException(e.getMessage());
        }
    }

    @Override
    public PaymentResponce createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params =SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/wallet?order_id"+orderId)
                .setCancelUrl("http://localhost:8080/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder().
                        setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmount(amount*100)
                                        .setProductData(SessionCreateParams
                                                .LineItem
                                                .PriceData
                                                .ProductData
                                                .builder()
                                                .setName("Top up wallet")
                                                .build()
                                        ).build()
                        ).build()
                ).build();


        Session session = Session.create(params);
        System.out.println("session____"+session);

        PaymentResponce res = new PaymentResponce();
        res.setPaymentUrl(session.getUrl());

        return res;
    }
}

