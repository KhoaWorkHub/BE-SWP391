package vn.fpt.diamond_shop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.fpt.diamond_shop.model.Address;
import vn.fpt.diamond_shop.model.EndUser;
import vn.fpt.diamond_shop.model.User;
import vn.fpt.diamond_shop.model.UserRole;
import vn.fpt.diamond_shop.repository.*;
import vn.fpt.diamond_shop.request.ChangePasswordRequest;
import vn.fpt.diamond_shop.request.ChangeProfileRequest;
import vn.fpt.diamond_shop.request.SignUpRequest;
import vn.fpt.diamond_shop.response.ImageInformation;
import vn.fpt.diamond_shop.response.UserProfileResponse;
import vn.fpt.diamond_shop.security.exception.BadRequestException;
import vn.fpt.diamond_shop.security.model.AuthProvider;
import vn.fpt.diamond_shop.security.model.RoleEnum;
import vn.fpt.diamond_shop.service.ImageService;
import vn.fpt.diamond_shop.service.Impl.OtpService;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;

@Service
public class AccountService {

    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final EndUserRepository endUserRepository;
    private final ImageService imageService;
    private final OtpService otpService;

    @Autowired
    public AccountService(AddressRepository addressRepository, PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository, EndUserRepository endUserRepository,
            ImageService imageService, OtpService otpService) {
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.endUserRepository = endUserRepository;
        this.imageService = imageService;
        this.otpService = otpService;
    }

    @Transactional
    public void register(SignUpRequest registerRequest) {
        validateEmail(registerRequest.getEmail());
        validateOtp(registerRequest.getEmail(), registerRequest.getOtp());

        User user = createUser(registerRequest);
        createUserRole(user.getId());
        createAddress(registerRequest);
        createEndUser(registerRequest, user.getId());
    }

    @Transactional
    public void registerV2(SignUpRequest registerRequest) {
        validateEmail(registerRequest.getEmail());

        User user = createUser(registerRequest);
        createUserRole(user.getId());
        createAddress(registerRequest);
        createEndUser(registerRequest, user.getId());
    }

    public void changeProfile(ChangeProfileRequest changeProfileRequest) {
        User account = findUserByEmail(changeProfileRequest.getEmail());
        EndUser endUser = findEndUserByAccountId(account.getId());
        Address address = findAddressOrCreateNew(endUser.getAddress());

        updateAddress(changeProfileRequest, address);
        updateEndUser(changeProfileRequest, endUser);
    }

    public UserProfileResponse profile(Long accountId) {
        User account = findUserById(accountId);
        EndUser endUser = findEndUserByAccountId(accountId);
        Address address = findAddressById(endUser.getAddress());

        return buildUserProfileResponse(account, endUser, address);
    }

    public void updateAvt(Long accountId, MultipartFile file) {
        User account = findUserById(accountId);
        ImageInformation imageInformation = imageService.push(file);
        account.setImageUrl(imageInformation.getImageName());
        userRepository.save(account);
    }

    public void changePass(ChangePasswordRequest request) {
        User account = findUserByEmail(request.getEmail());
        validateNewPassword(request, account);
        validateOtp(request.getEmail(), request.getOtp());

        account.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(account);
        otpService.deleteOtp(request.getEmail());
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email address already in use.");
        }
    }

    private void validateOtp(String email, String otp) {
        if (otpService.getOtp(email) == null || !otp.equals(otpService.getOtp(email))) {
            throw new BadRequestException("Invalid OTP");
        }
        otpService.deleteOtp(email);
    }

    private User createUser(SignUpRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return userRepository.save(user);
    }

    private void createUserRole(Long userId) {
        UserRole userRole = new UserRole();
        userRole.setRoleId(RoleEnum.END_USER.getId());
        userRole.setAccountId(userId);
        userRoleRepository.save(userRole);
    }

    private void createAddress(SignUpRequest registerRequest) {
        Address address = new Address();
        address.setProvince(registerRequest.getProvince());
        address.setDistrict(registerRequest.getDistrict());
        address.setCity(registerRequest.getCity());
        address.setWard(registerRequest.getWard());
        address.setExtra(registerRequest.getExtra());
        addressRepository.save(address);
    }

    private void createEndUser(SignUpRequest registerRequest, Long userId) {
        EndUser endUser = new EndUser();
        endUser.setPhoneNumber(registerRequest.getPhoneNumber());
        endUser.setFullName(registerRequest.getName());
        endUser.setCreateAt(OffsetDateTime.now());
        endUser.setAccountId(userId);
        endUser.setAddress(addressRepository.findById(userId).get().getId());
        endUserRepository.save(endUser);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
    }

    private User findUserById(Long accountId) {
        return userRepository.findById(accountId).orElseThrow(() -> new BadRequestException("User not found"));
    }

    private EndUser findEndUserByAccountId(Long accountId) {
        return endUserRepository.findEndUserByAccountId(accountId)
                .orElseThrow(() -> new BadRequestException("EndUser not found"));
    }

    private Address findAddressById(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(() -> new BadRequestException("Address not found"));
    }

    private Address findAddressOrCreateNew(Long addressId) {
        return addressId != null ? findAddressById(addressId) : new Address();
    }

    private void updateAddress(ChangeProfileRequest changeProfileRequest, Address address) {
        address.setProvince(changeProfileRequest.getProvince());
        address.setDistrict(changeProfileRequest.getDistrict());
        address.setCity(changeProfileRequest.getCity());
        address.setWard(changeProfileRequest.getWard());
        address.setExtra(changeProfileRequest.getExtra());
        addressRepository.save(address);
    }

    private void updateEndUser(ChangeProfileRequest changeProfileRequest, EndUser endUser) {
        endUser.setFullName(changeProfileRequest.getFullName());
        endUser.setPhoneNumber(changeProfileRequest.getPhoneNumber());
        endUser.setDateOfBirth(changeProfileRequest.getDateOfBirth());
        endUser.setAge(changeProfileRequest.getAge());
        endUser.setUpdateAt(OffsetDateTime.now());
        endUserRepository.save(endUser);
    }

    private UserProfileResponse buildUserProfileResponse(User account, EndUser endUser, Address address) {
        UserProfileResponse response = new UserProfileResponse();
        response.setEmail(account.getEmail());
        response.setFullName(endUser.getFullName());
        response.setPhoneNumber(endUser.getPhoneNumber());
        response.setAge(endUser.getAge() == null ? 0 : endUser.getAge());
        response.setDateOfBirth(endUser.getDateOfBirth());
        response.setAvatar(account.getImageUrl());
        response.setAddress(address);
        return response;
    }

    private void validateNewPassword(ChangePasswordRequest request, User account) {
        if (passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new BadRequestException("New password must be different from old password");
        }
    }
}
