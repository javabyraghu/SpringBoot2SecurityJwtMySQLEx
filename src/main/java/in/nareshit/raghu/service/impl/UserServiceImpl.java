package in.nareshit.raghu.service.impl;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.nareshit.raghu.model.User;
import in.nareshit.raghu.repo.UserRepository;
import in.nareshit.raghu.service.IUserService;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private BCryptPasswordEncoder encoder;

	public Integer saveUser(User user) {
		user.setPassword( 
				encoder.encode(
						user.getPassword()
						)
				);
		return repo.save(user).getId();
	}

	public User findByUsername(String username) {
		Optional<User> opt = repo.findByUsername(username);
		if(opt.isPresent())
			return opt.get();
		else 
			return null;
	}

	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		User user = findByUsername(username);
		if(user==null)
			throw new UsernameNotFoundException("User not exist");

		return new org.springframework.security.core.userdetails.User(
				username, user.getPassword(), 
				user.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toSet())
				);
	}

}
