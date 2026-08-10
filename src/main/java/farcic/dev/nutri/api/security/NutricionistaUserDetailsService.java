package farcic.dev.nutri.api.security;

import farcic.dev.nutri.api.repository.NutricionistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NutricionistaUserDetailsService implements UserDetailsService {

    private final NutricionistaRepository nutricionistaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return nutricionistaRepository.findByEmailIgnoreCase(email)
                .map(UsuarioAutenticado::from)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));
    }
}
