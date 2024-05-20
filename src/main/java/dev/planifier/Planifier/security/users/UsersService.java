package dev.planifier.Planifier.security.users;

import dev.planifier.Planifier.exception.UserNotFoundException;
import dev.planifier.Planifier.exception.UserWithEmailAlreadyExistsException;
import dev.planifier.Planifier.security.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("cannot find user with email: " + username));
    }

    public void registerNewUser(String name, String email, String password) throws UserWithEmailAlreadyExistsException {
        if (usersRepository.findByEmail(email).isPresent()) throw new UserWithEmailAlreadyExistsException();

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        usersRepository.save(user);
    }

    public boolean isUser(String id) {
        return usersRepository.existsById(id);
    }

    public boolean isPremium(String id) {
        if (!usersRepository.existsById(id)) throw new UserNotFoundException();

        return usersRepository.findById(id).get().isPremium();
    }

    public void premiumize(String id) {
        if (!usersRepository.existsById(id)) throw new UserNotFoundException();

        User user = usersRepository.findById(id).get();
        user.setPremium(true);

        usersRepository.save(user);
    }

    public void updateName(String id, String newName) {
        if (!usersRepository.existsById(id)) throw new UserNotFoundException();

        User user = usersRepository.findById(id).get();
        user.setName(newName);

        usersRepository.save(user);
    }
}
