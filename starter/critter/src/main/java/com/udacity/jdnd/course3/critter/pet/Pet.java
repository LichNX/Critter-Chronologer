package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private PetType type;

    @Nationalized
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    private LocalDate birthDate;

    private String notes;

    @ManyToMany(mappedBy = "pets", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

    public Pet() {
    }

    public Pet(long id, PetType type, String name, Customer customer, LocalDate birthDate, String notes) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.customer = customer;
        this.birthDate = birthDate;
        this.notes = notes;
    }

    public Pet(long id, PetType type, String name, Customer customer, LocalDate birthDate, String notes, List<Schedule> schedules) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.customer = customer;
        this.birthDate = birthDate;
        this.notes = notes;
        this.schedules = schedules;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
