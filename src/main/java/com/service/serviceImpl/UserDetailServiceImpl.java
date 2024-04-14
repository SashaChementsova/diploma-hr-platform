package com.service.serviceImpl;

import com.dto.UserDto;
import com.model.Image;
import com.model.Role;
import com.model.UserDetail;
import com.repository.ImageRepository;
import com.repository.RoleRepository;
import com.repository.UserDetailRepository;
import com.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailService {
    private final UserDetailRepository userDetailRepository;
    private final RoleRepository roleRepository;
    private final ImageRepository imageRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailServiceImpl(UserDetailRepository userDetailRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,ImageRepository imageRepository){
        this.userDetailRepository = userDetailRepository;
        this.roleRepository=roleRepository;
        this.passwordEncoder=passwordEncoder;
        this.imageRepository=imageRepository;
    }

    @Override
    public UserDetail saveUser(UserDto userDto, String roleName) throws IOException {
        UserDetail userDetails =fromUserDtoToUserDetails(userDto);
        Role role = roleRepository.findByNameRole(roleName);
        if(role == null){
            role = checkRoleExist(roleName);
        }
        Image image=new Image();
        if(userDto.getFile1().getSize()!=0){
            image=toImageEntity(userDto.getFile1());
        }
        else{
            image=getDefaultPicture();
        }
        userDetails.setImage(image);
        userDetails.setRoles(Arrays.asList(role));
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        image.setUserDetail(userDetails);
        return userDetailRepository.save(userDetails);
    }

    @Override
    public UserDetail saveUser(UserDetail userDetail, String roleName) throws IOException {
        Role role = roleRepository.findByNameRole(roleName);
        if(role == null){
            role = checkRoleExist(roleName);
        }
        Image image=getDefaultPicture();
        userDetail.setImage(image);
        userDetail.setRoles(Arrays.asList(role));
        userDetail.setPassword(passwordEncoder.encode(userDetail.getPassword()));
        return userDetailRepository.save(userDetail);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    @Override
    public UserDetail updateUser(UserDetail user){
        return userDetailRepository.save(user);
    }

    @Override
    public List<UserDto> getUsers(){
        List<UserDetail> users = userDetailRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetail findUserById(int id){

        return userDetailRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(int id){
        userDetailRepository.deleteById(id);
    }

    private UserDto mapToUserDto(UserDetail user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist(String roleName){
        Role role = new Role();
        role.setNameRole(roleName);
        return roleRepository.save(role);
    }

    @Override
    public UserDetail findUserByPhone(String phone) {
        return userDetailRepository.findByPhone(phone);
    }

    @Override
    public UserDetail findUserByEmail(String email) {
        return userDetailRepository.findByEmail(email);
    }
    @Override
    public int calculateAge(String dateOfBirth){
        int age=0;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOne = null;
        Date dateTwo = null;
        try {
            dateOne = format.parse(date);
            dateTwo = format.parse(dateOfBirth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Количество дней между датами в миллисекундах
        long difference = dateOne.getTime() - dateTwo.getTime();
        // Перевод количества дней между датами из миллисекунд в дни
        double days = difference / (24 * 60 * 60 * 1000); // миллисекунды / (24ч * 60мин * 60сек * 1000мс)
        double years=days/365;
        age= (int) Math.floor(years);
        return age;
    }

    private UserDetail fromUserDtoToUserDetails(UserDto userDto){
        UserDetail userDetail=new UserDetail();
        userDetail.setName(userDto.getName());
        userDetail.setPatronymic(userDto.getPatronymic());
        userDetail.setSurname(userDto.getSurname());
        userDetail.setPhone(userDto.getPhone());
        userDetail.setBirthday(userDto.getBirthday());
        userDetail.setEmail(userDto.getEmail());
        userDetail.setPassword(userDto.getPassword());
        userDetail.setInfo(userDto.getInfo());
        return userDetail;
    }

    private Image getDefaultPicture() throws IOException {
        Path path = Paths.get("D:\\bsuir\\7 семестр\\кп стоэи\\course_project_diploma\\src\\main\\resources\\static\\photos\\user.jpg");
        String name = "user.jpg";
        String originalFileName = "user.jpg";
        String contentType = "image/jpeg";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            System.out.println("ОШИБКА ПРИ ИНИЦИАЛИЗАЦИИ IMAGE");
        }
        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);
        Image image = new Image();
        image.setName(result.getName());
        image.setOriginalFileName(result.getOriginalFilename());
        image.setContentType(result.getContentType());
        image.setSize(result.getSize());
        image.setBytes(result.getBytes());
        return image;
    }
    @Override
    public boolean checkPassword(UserDetail user,String password){
        return passwordEncoder.matches(password,user.getPassword());
    }
    @Override
    public UserDetail savePassword(UserDetail user,String password){
        user.setPassword(passwordEncoder.encode(password));
        return userDetailRepository.save(user);
    }
}
