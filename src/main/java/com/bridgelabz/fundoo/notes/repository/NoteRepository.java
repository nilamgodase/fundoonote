package com.bridgelabz.fundoo.notes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.notes.model.Notes;





@Repository
public interface NoteRepository extends JpaRepository<Notes, Long>{
	/**
	 * Purpose : Get note by userid and noteid
	 * @param id
	 * @param userId
	 * @return
	 */
	public Notes findByIdAndUserId(long id , long userId);
	/**
	 * Purpose : Get list of notes of user
	 * @param id
	 * @return
	 */
	public List<Notes> findByUserId(long userId);
	
	//public List<Label> findAllListLabelById(long id);
}