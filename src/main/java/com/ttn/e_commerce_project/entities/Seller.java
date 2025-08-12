package com.ttn.e_commerce_project.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seller
{
    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String gst;
    private Long companyContact;
    private String companyName;
}
