package com.bridgelabz.fundoo.notes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.notes.dto.NoteDto;
import com.bridgelabz.fundoo.response.Response;




/**
 * Purpose : Service class for notes
 * @author admin1
 *
 */
@Service
public interface NoteService {

	/**
	 * Purpose : Function for creating notes
	 * @param notesDto
	 * @param token
	 * @return
	 */
	public Response createNote(NoteDto notesDto , String token);
	/**
	 * Purpose : Function for updating notes
	 * @param notesDto
	 * @param token
	 * @param noteId
	 * @return
	 */
	public Response updateNote(NoteDto notesDto,String token,long noteId);
	/**
	 * Purpose : Function for deleting notes
	 * @param token
	 * @param noteId
	 * @return
	 */
	public Response delete(String token , long noteId);
	/**
	 * Purpose : Function to get all the notes
	 * @param token
	 * @return
	 */
	public List<NoteDto>  getAllNotes(String token);
	/**
	 * Purpose : Function to pin or unpin notes 
	 * @param token
	 * @param noteId
	 * @return
	 */
	public Response pinAndUnPin(String token , long noteId);
	/**
	 * Purpose : Function to archive or unarchive notes 
	 * @param token
	 * @param noteId
	 * @return
	 */
	public Response archiveAndUnArchive(String token , long noteId);
	/**
	 * Purpose : Function to trash or untrash notes 
	 * @param token
	 * @param noteId
	 * @return
	 */
	public Response trashAndUnTrash(String token , long noteId);
	/**
	 * Purpose : Function to delete notes permanently
	 * @param token
	 * @param noteId
	 * @return
	 */
	public Response deletePermanently(String token , long noteId);
	/**
	 * Purpose : Function to get all archive notes
	 * @param token
	 * @return
	 */
	public List<NoteDto> getArchiveNotes(String token);
	/**
	 * Purpose : Function to get all trash notes 
	 * @param token
	 * @return
	 */
	public List<NoteDto> getTrashNotes(String token);
}