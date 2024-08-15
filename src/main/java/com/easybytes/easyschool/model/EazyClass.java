package com.easybytes.easyschool.model;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "class")
public class EazyClass extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private int classId;

	@NotBlank(message = "Name must not be blank")
	@Size(min = 3, message = "Name must be at least 3 characters long")
	private String name;

	@OneToMany(mappedBy = "eazyClass", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, targetEntity = Person.class)
	private Set<Person> persons;

}
