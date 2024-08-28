package com.easybytes.easyschool.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.easybytes.easyschool.configuration.EazySchoolProps;
import com.easybytes.easyschool.model.Contact;
import com.easybytes.easyschool.repository.ContactRepository;
import com.easybytes.easyschoolconstants.EazySchoolConstants;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	EazySchoolProps eazySchoolProps;

	private static Logger log = LoggerFactory.getLogger(ContactService.class);

	/*
	 * save the contact details into DB
	 * 
	 * @param contact
	 * 
	 * @return boolean
	 */

	public boolean saveMessageDetails(Contact contact) {
		boolean isSaved = false;
		contact.setStatus(EazySchoolConstants.OPEN);
		// below code is done using JPA Auditing
		// contact.setCreatedBy(EazySchoolConstants.ANONYMOUS);
		// contact.setCreatedAt(LocalDateTime.now());

		Contact result = contactRepository.save(contact);
		if (null != result && result.getContactId() > 0) {
			isSaved = true;
		}
		// log.info(contact.toString());
		return isSaved;
	}

	public Page<Contact> findMsgsWithOpenStatus(int pageNum, String sortField, String sortDir) {
		int pageSize = eazySchoolProps.getPageSize();
		if(null != eazySchoolProps.getContact() && null != eazySchoolProps.getContact().get("pageSize")) {
			pageSize = Integer.parseInt(eazySchoolProps.getContact().get("pageSize").trim());
		}
		PageRequest pageable = PageRequest.of(pageNum - 1, pageSize,
				sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
		Page<Contact> contactMsgs = contactRepository.findByStatus(EazySchoolConstants.OPEN, pageable);
		return contactMsgs;
	}

	public boolean updateMsgStatus(int contactId/* , String updatedBy */) {
		boolean isUpdated = false;
		Optional<Contact> contact = contactRepository.findById(contactId);
		contact.ifPresent(contact1 -> {
			contact1.setStatus(EazySchoolConstants.CLOSE);
			// below code is done using JPA Auditing
			// contact1.setUpdatedBy(updatedBy);
			// contact1.setUpdatedAt(LocalDateTime.now());
		});

		Contact result = contactRepository.save(contact.get());
		if (null != result && result.getContactId() > 0) {
			isUpdated = true;
		}
		return isUpdated;
	}

}
