package com.istepien.controller;

import com.istepien.model.Document;
import com.istepien.model.Message;
import com.istepien.model.Role;
import com.istepien.model.User;
import com.istepien.service.DocumentService;
import com.istepien.service.MessageService;
import com.istepien.service.RoleService;
import com.istepien.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private DocumentService documentService;

    @GetMapping("/requestForUpgrade")
    public String requestForUpgrade(@RequestParam(name = "username") String username, Model model) {
        Message message = new Message();
        message.setUser(userService.getUserByName(username));
        messageService.saveMessage(message);

        return "home";
    }

    @GetMapping("/getAllUpgradeRequests")
    public String getAllUpgradeRequests(Model model) {
        List<Message> messageList = messageService.getAllMessages();

        List<String> toBeUpgraded = new ArrayList<>();
        for (Message m : messageList) {
            toBeUpgraded.add(m.getUser().getUsername());
        }
        model.addAttribute("toBeUpgraded", toBeUpgraded);


        return "list";
    }


    @GetMapping("/upgradeRole")
    public String upgradeRole(@RequestParam(name = "username") String username) {

        User current = userService.getUserByName(username);
        Role moderator = roleService.getRoleByName("ROLE_MODERATOR");
        current.setRole(moderator);
        userService.updateUser(current);
        return "home";
    }

    @GetMapping("/documentToBeDeleted")
    public String AddDocumentToBeDeleted(@RequestParam(name = "docId") Long docId, Model model, Principal principal) {
        Message message = new Message();

        message.setDocument(documentService.getDocument(docId));
        message.setUser(userService.getUserByName(principal.getName()));

        messageService.saveMessage(message);


        return "allDocuments-list";
    }

    @GetMapping("/getAllDocumentsToBeDeleted")
    public String getAllDocumentsToBeDeleted(Model model) {
        List<Message> messageList = messageService.getAllMessages();

        List<Document> toBeDeleted = new ArrayList<>();
        for ( Message message : messageList){
          toBeDeleted.add(message.getDocument());
        }

        model.addAttribute("toBeDeleted", toBeDeleted);

        return "documents-toBeDeleted";
    }
}
