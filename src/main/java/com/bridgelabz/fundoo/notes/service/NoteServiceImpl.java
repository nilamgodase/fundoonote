package com.bridgelabz.fundoo.notes.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.notes.dto.NoteDto;
import com.bridgelabz.fundoo.notes.model.Notes;
import com.bridgelabz.fundoo.notes.repository.NoteRepository;
import com.bridgelabz.fundoo.repository.UserRepository;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenUtil;

@Service("notesService")
@PropertySource("classpath:message.properties")
public class NoteServiceImpl implements NoteService {

	Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

	@Autowired
	private TokenUtil userToken;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository notesRepository;

	@Autowired
	private Environment environment;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgelabz.fundoo.notes.service.INotesService#createNote(com.bridgelabz.
	 * fundoo.notes.dto.NotesDto, java.lang.String)
	 */

	public Response createNote(NoteDto noteDto, String token) {

		long id = userToken.decodeToken(token);
		logger.info(noteDto.toString());
		if (noteDto.getTitle().isEmpty() && noteDto.getDescription().isEmpty()) {

			throw new UserException(-5, "Title and description are empty");

		}
		Notes notes = modelMapper.map(noteDto, Notes.class);
		Optional<com.bridgelabz.fundoo.entity.User> user = userRepository.findByUserId(id);
		notes.setUserId(id);
		notes.setCreated(LocalDateTime.now());
		notes.setModified(LocalDateTime.now());
		user.get().getNotes().add(notes);
		notesRepository.save(notes);
		userRepository.save(user.get());
		
		
		Response response=ResponseHelper.statusResponse(100, environment.getProperty("status.notes.createdSuccessfull"));
		return response;
		
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgelabz.fundoo.notes.service.INotesService#updateNote(com.bridgelabz.
	 * fundoo.notes.dto.NotesDto, java.lang.String, long)
	 */
	@Override
	public Response updateNote(NoteDto notesDto, String token, long noteId) {
		if (notesDto.getTitle().isEmpty() && notesDto.getDescription().isEmpty()) {
			throw new UserException(-5, "Title and description are empty");
		}

		long id = userToken.decodeToken(token);
		Notes notes = notesRepository.findByIdAndUserId(noteId, id);
		notes.setTitle(notesDto.getTitle());
		notes.setDescription(notesDto.getDescription());
		notes.setModified(LocalDateTime.now());
		notesRepository.save(notes);
		
		
		Response response=ResponseHelper.statusResponse(100, environment.getProperty("status.label.removedfromnote"));
		return response;
		
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgelabz.fundoo.notes.service.INotesService#delete(java.lang.String,
	 * long)
	 */
	@Override
	public Response delete(String token, long noteId) {
		long id = userToken.decodeToken(token);
		Notes notes = notesRepository.findByIdAndUserId(noteId, id);
		if (notes == null) {
			throw new UserException(-5, "Invalid input");
		}
		if (notes.isTrash() == false) {
			notes.setTrash(true);
			notes.setModified(LocalDateTime.now());
			notesRepository.save(notes);
			
			Response response=ResponseHelper.statusResponse(100, environment.getProperty("status.note.trashed"));
			return response;
			
			}
		Response response=ResponseHelper.statusResponse(100, environment.getProperty("status.note.trashError"));
		return response;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bridgelabz.fundoo.notes.service.INotesService#getAllNotes(java.lang.
	 * String)
	 */
	@Override
	public List<NoteDto> getAllNotes(String token) {
		long id = userToken.decodeToken(token);
		List<Notes> notes = (List<Notes>) notesRepository.findByUserId(id);
		List<NoteDto> listNotes = new ArrayList<>();
		for (Notes userNotes : notes) {
			NoteDto notesDto = modelMapper.map(userNotes, NoteDto.class);
			if (userNotes.isArchive() == false && userNotes.isTrash() == false) {
				listNotes.add(notesDto);

			}
		}
		return listNotes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bridgelabz.fundoo.notes.service.INotesService#pinAndUnPin(java.lang.
	 * String, long)
	 */
	@Override
	public Response pinAndUnPin(String token, long noteId) {
		long id = userToken.decodeToken(token);
		Notes notes = notesRepository.findByIdAndUserId(noteId, id);
		if (notes == null) {
			throw new UserException(-5, "Invalid input");
		}
		if (notes.isPin() == false) {
			notes.setPin(true);
			notesRepository.save(notes);

			Response response = ResponseHelper.statusResponse(100,
					environment.getProperty("status.note.pinned"));
			return response;

		
		} else {
			notes.setPin(false);
			notesRepository.save(notes);

			Response response = ResponseHelper.statusResponse(100,
					environment.getProperty("status.note.unpinned"));
			return response;

			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgelabz.fundoo.notes.service.INotesService#archiveAndUnArchive(java.
	 * lang.String, long)
	 */
	@Override
	public Response archiveAndUnArchive(String token, long noteId) {
		long id = userToken.decodeToken(token);
		Notes notes = notesRepository.findByIdAndUserId(noteId, id);
		if (notes == null) {
			throw new UserException(-5, "Invalid input");
		}
		if (notes.isArchive() == false) {
			notes.setArchive(true);
			notesRepository.save(notes);

			Response response = ResponseHelper.statusResponse(100,
					environment.getProperty("status.note.archieved"));
			return response;

		
		} else {
			notes.setArchive(false);
			notesRepository.save(notes);

			Response response = ResponseHelper.statusResponse(100,
					environment.getProperty("status.note.unarchieved"));
			return response;

	
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgelabz.fundoo.notes.service.INotesService#trashAndUnTrash(java.lang.
	 * String, long)
	 */
	@Override
	public Response trashAndUnTrash(String token, long noteId) {
		long id = userToken.decodeToken(token);
		Notes notes = notesRepository.findByIdAndUserId(noteId, id);
		if (notes.isTrash() == false) {
			notes.setTrash(true);
			notesRepository.save(notes);

			Response response = ResponseHelper.statusResponse(100,
					environment.getProperty("status.note.trashed"));
			return response;

		} else {
			notes.setTrash(false);
			notesRepository.save(notes);

			Response response = ResponseHelper.statusResponse(100,
					environment.getProperty("status.note.untrashed"));
			return response;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgelabz.fundoo.notes.service.INotesService#deletePermanently(java.lang
	 * .String, long)
	 */
	@Override
	public Response deletePermanently(String token, long noteId) {
		long id = userToken.decodeToken(token);
		Notes notes = notesRepository.findByIdAndUserId(noteId, id);
		if (notes.isTrash() == true) {
			notesRepository.delete(notes);

			Response response = ResponseHelper.statusResponse(100,
					environment.getProperty("status.note.deleted"));
			return response;

		
		} else {

			Response response = ResponseHelper.statusResponse(100, environment.getProperty("status.note.notdeleted"));
			return response;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgelabz.fundoo.notes.service.INotesService#getArchiveNotes(java.lang.
	 * String)
	 */
	@Override
	public List<NoteDto> getArchiveNotes(String token) {
		long id = userToken.decodeToken(token);
		List<Notes> notes = (List<Notes>) notesRepository.findByUserId(id);
		List<NoteDto> listNotes = new ArrayList<>();
		for (Notes userNotes : notes) {
			NoteDto notesDto = modelMapper.map(userNotes, NoteDto.class);
			if (userNotes.isArchive() == true) {
				listNotes.add(notesDto);
			}
		}
		return listNotes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgelabz.fundoo.notes.service.INotesService#getTrashNotes(java.lang.
	 * String)
	 */
	@Override
	public List<NoteDto> getTrashNotes(String token) {
		long id = userToken.decodeToken(token);
		List<Notes> notes = (List<Notes>) notesRepository.findByUserId(id);
		List<NoteDto> listNotes = new ArrayList<>();
		for (Notes userNotes : notes) {
			NoteDto notesDto = modelMapper.map(userNotes, NoteDto.class);
			if (userNotes.isTrash() == true) {
				listNotes.add(notesDto);
			}
		}
		return listNotes;
	}

	


}