package br.sc.senai.controller;

import br.sc.senai.model.Profile;
import br.sc.senai.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping(path = "/profiles")
    public @ResponseBody ResponseEntity<Profile> addNewProfile(@RequestBody Profile profile) {
        try {
            Profile newProfile = profileRepository.save(profile);
            return new ResponseEntity<>(newProfile, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping(path = "/profiles")
    public @ResponseBody ResponseEntity<Iterable<Profile>> getAllProfiles() {
        try {
            Iterable<Profile> profiles = profileRepository.findAll();
            if(((Collection<?>) profiles).size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(profiles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/profiles/{id}")
    public @ResponseBody ResponseEntity<Profile> updateProfile(@PathVariable("id") Integer id, @RequestBody Profile profile) {
        Optional<Profile> profileData = profileRepository.findById(id);

        if(profileData.isPresent()) {
            try {
                Profile updatedProfile = profileData.get();
                updatedProfile.setDescription(profile.getDescription());
                return new ResponseEntity<>(profileRepository.save(updatedProfile), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/profiles/{id}")
    public @ResponseBody ResponseEntity<HttpStatus> removeProfile(@PathVariable("id") Integer id) {
        try {
            profileRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
