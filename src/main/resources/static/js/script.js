console.log("This is javascript");


const toggleSidebar = () => {
    // sidebar ko band krna he three line pr click karke
    if ($(".sidebar").is(":visible")) {
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");

    }
    else {
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
}
const search = () => {
    // console.log("searching....")

    let query = $("#search-input").val();

    if (query === '') {
        $("#search-result").hide();
    } else {
        // sending request to server
        let url = `http://localhost:9090/search/${query}`;
        fetch(url)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                console.log(data);

                let text = `<div class='list-group'>`;
                data.forEach((contact) => {
                    text += `<a href='/user/contact/${contact.cid}' class='list-group-item list-group-item-action'>${contact.name}</a>`
                });
                text += `</div>`;

                $(".search-result").html(text);
                $(".search-result").show();
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }
};

//   for payment intregration 
// hum sabse phle to client side se request bhejenge server pr 
const paymentStart = () => {
    let amount = $("#payment_Field").val();  // this is a jquiry
    console.log(amount)

    if (amount == '' || amount == null | amount == ' ') {
        alert("Please Enter Amount");
        return;
    }
    else if (isNaN(amount)) {
        alert("Amount must be a valid number");
        return;
    }

    // ajax ka use hum server pr reques send kanne ke liye karte he 

    $.ajax(
        {
            url: '/user/create_order',
            data: JSON.stringify({ amount: amount, info: 'order_request' }),
            contentType: 'application/json',
            type: 'POST',
            datatype: 'json',
            success: function (response) {
                // jab success ho tb chalega
                console.log(typeof response);
                response = JSON.parse(response);
                console.log("this is response");
                if (response.status == "created") {
                    console.log("if condition is working : ");
                    // open form
                    let options = {
                        key: 'rzp_test_FzNm8xwjZR6SCt',
                        amount: response.amount,
                        currency: 'INR',
                        name: 'Smart Contact Manager',
                        description: 'Donation',
                        Image:"/image/Razorpay_logo.webp.png",
                        order_id: response.id,
                        handler: function (response) {
                            console.log(response.razorpay_payment_id)
                            console.log(response.razorpay_order_id)
                            console.log(response.razorpay_signature)
                            console.log("payment successfully ");

                            updatePaymentOnServer(
                                response.razorpay_payment_id,
                                response.razorpay_order_id,
                                "paid"
                            );
                            // alert("Payment Succesfully !! ")


                        },
                        prefill: {
                            "name": "",
                            "email": "",
                            "contact": ""
                        },
                        // notes: {
                        //     "address": "Razorpay Corporate Office"
                        //     },
                        theme: {
                            "color": "#3399cc"
                        }
                    };
                    let rzp1 = new Razorpay(options);    //set the option  to razorpay
                    rzp1.on('payment.failed', function (response) {
                        console.log(response.error.code);
                        console.log(response.error.description);
                        console.log(response.error.source);
                        console.log(response.error.step);
                        console.log(response.error.reason);
                        console.log(response.error.metadata.order_id);
                        console.log(response.error.metadata.payment_id);
                        // alert("Payment Failed !! ")
                    });
                    // jab payment success ho jaye 

                    //for open the form  
                    rzp1.open();
                    return;
                }
            },
            error: function (error) {
                console.log(error);
                alert("something went wrong")
            }
        }
    )
}

// for update the payment 
 function updatePaymentOnServer(payment_id, order_id, status)
{
    $.ajax(
        {
            url: '/user/update_order',
            data: JSON.stringify({
                payment_id: payment_id,
                order_id: order_id,
                status: status,
            }),
            contentType: 'application/json',
            type: 'POST',
            datatype: 'json',
            success: function (response) {
                alert("Payment Succesfully !! ")

            },
            error: function (error) {
                alert("Your payment is succesfull but not on the server so we will contact you !! ")
            },
        });
}



