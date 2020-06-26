package kakaopay.spray.service.Impl;

import kakaopay.spray.persistence.UserRepository;
import kakaopay.spray.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public BigInteger getSeq(BigInteger userId) {
        return userRepository.findSeqByUserId(userId);
    }
}
