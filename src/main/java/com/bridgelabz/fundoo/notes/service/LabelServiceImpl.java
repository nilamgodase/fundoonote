package com.bridgelabz.fundoo.notes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.notes.dto.LabelDto;
import com.bridgelabz.fundoo.notes.dto.NoteDto;
import com.bridgelabz.fundoo.notes.exception.LabelException;
import com.bridgelabz.fundoo.notes.exception.NoteException;
import com.bridgelabz.fundoo.notes.exception.TokenException;
import com.bridgelabz.fundoo.notes.model.Label;
import com.bridgelabz.fundoo.notes.model.Notes;
import com.bridgelabz.fundoo.notes.repository.LabelRepository;
import com.bridgelabz.fundoo.notes.repository.NoteRepository;

import com.bridgelabz.fundoo.repository.UserRepository;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenUtil;




@Service("labelService")
@PropertySource("classpath:message.properties")
public class LabelServiceImpl implements LabelService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private LabelRepository labelRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NoteRepository notesRepository;
	
	@Autowired
	private TokenUtil userToken;
	
	@Autowired
	private Environment environment;
	
	/* (non-Javadoc)
	 * @see com.bridgelabz.fundoo.label.service.ILabelService#createLabel(com.bridgelabz.fundoo.label.dto.LabelDto, java.lang.String)
	 */
	@Override
	public Response createLabel(LabelDto labelDto, String token) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new LabelException("Invalid input", -6);
		}
		if(labelDto.getLabelName().isEmpty()) {
			throw new LabelException("Label has no name", -6);
		}
		Optional<Label> labelAvailability = labelRepository.findByUserIdAndLabelName(userId, labelDto.getLabelName());
		if(labelAvailability.isPresent()) {
			throw new LabelException("Label already exist", -6);
		}
		
		Label label = modelMapper.map(labelDto,Label.class);
		
		label.setLabelName(labelDto.getLabelName());
		label.setUserId(userId);
		label.setCreatedDate(LocalDateTime.now());
		label.setModifiedDate(LocalDateTime.now());
		labelRepository.save(label);
		user.get().getLabel().add(label);
		userRepository.save(user.get());
		Response response=ResponseHelper.statusResponse(100, environment.getProperty("status.label.createdSuccessfull"));
		return response;
		
	}

	/* (non-Javadoc)
	 * @see com.bridgelabz.fundoo.label.service.ILabelService#deleteLabel(long, java.lang.String)
	 */
	@Override
	public Response deleteLabel(long labelId, String token) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new LabelException("Invalid input", -6);
		}
		Label label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if(label == null) {
			throw new LabelException("Invalid input", -6);
		}
		labelRepository.delete(label);
		Response response=ResponseHelper.statusResponse(-5, environment.getProperty("status.labels.deletedtedSuccessfull"));
		return response;
		
	}

	/* (non-Javadoc)
	 * @see com.bridgelabz.fundoo.label.service.ILabelService#updateLabel(long, java.lang.String, com.bridgelabz.fundoo.label.dto.LabelDto)
	 */
	@Override
	public Response updateLabel(long labelId , String token ,LabelDto labelDto) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new LabelException("Invalid input", -6);
		}
		Label label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if(label == null ) {
			throw new LabelException("No label exist", -6);
		}
		if(labelDto.getLabelName().isEmpty()) {
			throw new LabelException("Label has no name", -6);
		}
		Optional<Label> labelAvailability = labelRepository.findByUserIdAndLabelName(userId, labelDto.getLabelName());
		if(labelAvailability.isPresent()) {
			throw new LabelException("Label already exist", -6);
		}
		label.setLabelName(labelDto.getLabelName());
		label.setModifiedDate(LocalDateTime.now());
		labelRepository.save(label);
		Response response=ResponseHelper.statusResponse(100, environment.getProperty("status.notes.updatedSuccessfull"));
		return response;
		
	}

	/* (non-Javadoc)
	 * @see com.bridgelabz.fundoo.label.service.ILabelService#getAllLabel(java.lang.String)
	 */
	@Override
	public List<LabelDto> getAllLabel(String token) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new LabelException("Invalid input", -6);
		}
		
		List<Label> labels = labelRepository.findByUserId(userId);
		List<LabelDto> listLabel = new ArrayList<>();
		for(Label noteLabel : labels) {
			LabelDto labelDto = modelMapper.map(noteLabel, LabelDto.class);
			listLabel.add(labelDto);
		}
		return listLabel;
	}

	/* (non-Javadoc)
	 * @see com.bridgelabz.fundoo.label.service.ILabelService#addNoteToLabel(long, java.lang.String, long)
	 */
	@Override
	public Response addLabelToNote(long labelId, String token , long noteId) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new LabelException("Invalid input", -6);
		}
		Label label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if(label == null) {
			throw new LabelException("No such lebel exist", -6);
		}
		Notes note =  notesRepository.findByIdAndUserId(noteId, userId);
		if(note == null) {
			throw new LabelException("No such note exist", -6);
		}
		label.setModifiedDate(LocalDateTime.now());
		label.getNotes().add(note);
		note.getListLabel().add(label);
		note.setModified(LocalDateTime.now());
		labelRepository.save(label);
		notesRepository.save(note);
		Response response=ResponseHelper.statusResponse(100, environment.getProperty("status.lales.addedtonoteSuccessfull"));
		return response;
		
	}

	

	/* (non-Javadoc)
	 * @see com.bridgelabz.fundoo.label.service.ILabelService#removeLabelFromNote(long, java.lang.String, long)
	 */
	@Override
	public Response removeLabelFromNote(long labelId ,String token , long noteId) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new LabelException("Invalid input", -6);
		}
		Label label = labelRepository.findByLabelIdAndUserId(labelId , userId);
		if(label == null) {
			throw new LabelException("No such lebel exist", -6);
		}
		Notes note =  notesRepository.findByIdAndUserId(noteId, userId);
		if(note == null) {
			throw new LabelException("No such note exist", -6);
		}
		label.setModifiedDate(LocalDateTime.now());
		note.getListLabel().remove(label);
		note.setModified(LocalDateTime.now());
		labelRepository.save(label);
		notesRepository.save(note);
		Response response=ResponseHelper.statusResponse(100, environment.getProperty("status.label.removedfromnoteSuccessfull"));
		return response;
		
	}

	/* (non-Javadoc)
	 * @see com.bridgelabz.fundoo.label.service.ILabelService#getLebelsOfNote(java.lang.String, long)
	 */
	@Override
	public List<LabelDto> getLebelsOfNote(String token, long noteId) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new LabelException("User does not exist", -6);
		}
		Optional<Notes> note = notesRepository.findById(noteId);
		if(!note.isPresent()) {
			throw new NoteException(-6,"Note does not exist");
		}
		List<Label> lebel = note.get().getListLabel();
		
		List<LabelDto> listLabel = new ArrayList<>();
		for(Label noteLabel : lebel) {
			LabelDto labelDto = modelMapper.map(noteLabel, LabelDto.class);
			listLabel.add(labelDto);
		}
		return listLabel;
		
	}
	
	/* (non-Javadoc)
	 * @see com.bridgelabz.fundoo.notes.service.ILabelService#getNotesOfLabel(java.lang.String, long)
	 */
	@Override
	public List<NoteDto> getNotesOfLabel(String token, long labelId) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if(!user.isPresent()) {
			throw new TokenException(-6,"Invalid input");
		}
		Optional<Label> label = labelRepository.findById(labelId);
		if(!label.isPresent()) {
			throw new LabelException("No lebel exist", -6);
		}
		List<Notes> notes = label.get().getNotes();
		List<NoteDto> listNotes = new ArrayList<>();
		for (Notes usernotes : notes) {
			NoteDto noteDto = modelMapper.map(usernotes, NoteDto.class);
			listNotes.add(noteDto);
		}
		return listNotes;
	}
	
	
}