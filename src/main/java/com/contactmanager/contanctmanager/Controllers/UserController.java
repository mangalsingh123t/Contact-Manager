package com.contactmanager.contanctmanager.Controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.contactmanager.contanctmanager.Entites.Contact;
import com.contactmanager.contanctmanager.Entites.MyOrder;
import com.contactmanager.contanctmanager.Entites.User;
// import com.contactmanager.contanctmanager.Entites.User;
import com.contactmanager.contanctmanager.Helper.Message;
import com.contactmanager.contanctmanager.Repositories.ContactRepository;
import com.contactmanager.contanctmanager.Repositories.OrderRepository;
// import com.contactmanager.contanctmanager.Repositories.UserRepository;
import com.contactmanager.contanctmanager.Repositories.UserRepository;

import jakarta.servlet.http.HttpSession;
// import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.razorpay.*;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;
   
    @Autowired
    OrderRepository orderRepository;


    @ModelAttribute
    public void add_CommonData(Model model, Principal principal) {
        String username = principal.getName();
        System.out.println(username);
        User user = userRepository.findUserByUserName(username);
        model.addAttribute("user", user);
    }

    // creating order for payment
    @PostMapping("/create_order")
    @ResponseBody // json ke form m =e data dalne ke liye
    public String createOrder(@RequestBody Map<String, Object> data,Principal principal) throws RazorpayException {
        System.out.println(data);
        int price = Integer.parseInt(data.get("amount").toString());
        // razorpay start
        RazorpayClient razorpay =  new RazorpayClient("rzp_test_FzNm8xwjZR6SCt", "1lokxaA4T8FeKGtsvPSSeXkr");

        //  new RazorpayClient("[YOUR_KEY_ID]", "[YOUR_KEY_SECRET]");

        // razore pay ki official documnetation pr uplabdh
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", price * 100); // indian currency ke liye * kr ahe amount pese me aayega
        orderRequest.put("currency", "INR"); // for indian currency

        // this is optional for rojorpay
        // orderRequest.put("receipt", "receipt#1");
        // JSONObject notes = new JSONObject();
        // notes.put("notes_key_1", "Tea, Earl Grey, Hot");
        // orderRequest.put("notes", notes);
        // Order order = instance.orders.create(orderRequest);

        // creating new order
        Order order = razorpay.Orders.create(orderRequest);
        System.out.println(order);
        
        // save data in the database
        MyOrder myOrder = new MyOrder();
        myOrder.setOrderId(order.get("id"));
        myOrder.setAmount(order.get("amount"));
        myOrder.setStatus(order.get("status"));
        myOrder.setPaymentd(null);
        myOrder.setUser(userRepository.findUserByUserName(principal.getName()));
        orderRepository.save(myOrder);
        System.out.println("order is created successfully : ");
        return order.toString();//response me data dene ke liye 
    }

    @PostMapping("/update_order")
    public ResponseEntity<?> updateOrder(@RequestBody Map<String,Object> data) {
           
          MyOrder myOrder = orderRepository.findByOrderId(data.get("order_id").toString());
           
          myOrder.setPaymentd(data.get("payment_id").toString());
          myOrder.setStatus(data.get("status").toString());

          orderRepository.save(myOrder);
          System.out.println(data);

         return ResponseEntity.ok("");
    }
    

    @RequestMapping("/index")
    public String getUserDashbourd(Model model, Principal principal) {
        return "normal_user/user_dasbourd";
    }

    // handler
    @GetMapping("/add_contact")
    public String add_contact(Model model, Principal principal) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal_user/add_contactForm";
    }

    @PostMapping("/process-contact")
    // Important ***** jo bhi data form and contact ke data me match karga vo sab
    // contact ke obj me aajayga automatic and jo match nhi karega vo @param me aa
    // jayga jes file etc
    public String saveContact(@ModelAttribute Contact contact, @RequestParam("profileimage") MultipartFile file,
            Principal principal, HttpSession session) {
        try {
            // principle means jo user logged in he uska name isme aayea mtlab email
            String name = principal.getName();
            User user = userRepository.findUserByUserName(name);
            // String uid = user.getUid();
            System.out.println(
                    "-------------------------->>>>>>>>>>>->>>>>>>>>>>________+++++>>>>>>>>>>>-------------------");
            if (file.isEmpty()) { // emty method null ko check nhi karti he jabli db me yadi kpi data
                                  // nhi hota he
                // to to data nullhota he
                contact.setImage("contacts.png");
                System.out.println("User have not selected the image : ");
            } else {
                // isse jo bhi file ka name hum select karenge uska name mil jayega
                contact.setImage(file.getOriginalFilename());

                // for targeting the file path in to store the image
                File savefile = new ClassPathResource("static/image").getFile();

                Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                // jb project build hota he to image direct target folder me chali jati he open
                // folder and see target
                System.out.println("Image is uploaded successfully : ");
                session.setAttribute("message", new Message("Contact Added Successfully", "success"));
            }
            contact.setUser(user);
            contactRepository.save(contact);
            System.out.println(contact);
        } catch (Exception e) {
            session.setAttribute("message", new Message("Something went wrong !!", "danger"));
            // isko cutimize krna he
            // session.setAttribute("message",new Message(e.getMessage(), "danger"));
            e.printStackTrace();
        }

        return "normal_user/add_contactForm";
    }

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "user contacts");
        String username = principal.getName();
        User user = userRepository.findUserByUserName(username);
        Pageable pagerequest = PageRequest.of(page, 3);
        Page<Contact> contacts = contactRepository.findContactsByUserId(user.getUid(), pagerequest);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentpage", page);
        model.addAttribute("totalpages", contacts.getTotalPages());
        return "normal_user/show_contacts";
    }

    @GetMapping("/contact/{cid}")
    public String showContactDetails(@PathVariable("cid") String cid, Model model) {
        // yah null hone pr exption nhi show krta he
        Optional<Contact> optionalContact = contactRepository.findById(cid);
        // read this method
        Contact contactInfo = optionalContact.get();

        System.out.println("contact Information : " + contactInfo);
        model.addAttribute("contactInfo", contactInfo);
        return "normal_user/contactdetail";
    }

    @GetMapping("/deletecontact/{cid}")
    public String deleteContact(@PathVariable("cid") String cid, Model model, HttpSession session) {

        contactRepository.deleteById(cid);
        session.setAttribute("message", new Message("Content Deleted Successfully", "Deleted"));
        return "redirect:/user/show-contacts/0";
    }

    @GetMapping("/update_contact/{cid}")
    public String updateContactForm(Model model, @PathVariable("cid") String cid) {
        model.addAttribute("title", "Add Contact");
        Contact contact = contactRepository.findContactsByContactId(cid);
        model.addAttribute("contact", contact);
        // contact.setName(contacts.getName());
        // contact.setSurname(contacts.getSurname());
        // contact.setEmail(contacts.getEmail());
        // contact.setPhone(contacts.getPhone());
        // contact.setWork(contacts.getWork());
        // contact.setDiscription(contacts.getDiscription());
        // contact.setImage(null);
        // System.out.println("contact Information :
        // "+contactRepository.save(contacts));
        return "normal_user/updateContact";
    }

    @PostMapping("/update-contact/{cid}")
    public String updateContact(@ModelAttribute Contact contact, @RequestParam("profileimage") MultipartFile file,
            @PathVariable("cid") String cid) {
        if (file.isEmpty()) {
            contact.setImage("contacts.png");
        }
        contact.setCid(cid);
        contactRepository.save(contact);
        return "normal_user/updateContact";
    }

    // setting handler
    @GetMapping("/setting")
    public String getSettings() {
        return "normal_user/setting";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {
        System.out.println("This is a old password you have Entered : " + oldPassword);
        String currentUser = principal.getName();
        User user = userRepository.findUserByUserName(currentUser);
        System.out.println("This is password stored in the database : " + user.getPassword());

        if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user);
            System.out.println("Your password is changed succesfully : " + user.getPassword());
            session.setAttribute("message", new Message("Your Password is Changed Succesfully", "success"));
        } else {
            session.setAttribute("message", new Message("please Enter your old password currect", "danger"));
            return "redirect:/user/setting";
        }
        return "redirect:/user/index";
    }

    // @GetMapping("/searchcontacts/{name}")
    // public String getMethodName(@PathVariable("name") String name) {
    // contactRepository.findByCustumQuiry(name);
    // return "normal_user/show_contacts";

    // }

}
