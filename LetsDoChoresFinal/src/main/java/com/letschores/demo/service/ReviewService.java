package com.letschores.demo.service;

import com.letschores.demo.model.Chore;
import com.letschores.demo.model.Player;
import com.letschores.demo.model.Review;
import com.letschores.demo.repository.ChoreRepository;
import com.letschores.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ChoreService choreService;
    private PlayerService playerService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ChoreService choreService, PlayerService playerService) {
        this.reviewRepository = reviewRepository;
        this.choreService = choreService;
        this.playerService = playerService;
    }

    @Transactional
    public Review createReview(Chore chore){
        LocalDate currentDate= LocalDate.now();
        Review review=Review.builder()
                .description(chore.getDescription())
                .duration(chore.getDuration())
                .difficulty(chore.getDifficulty())
                .value(chore.getValue())
                .localDate(currentDate)
                .chore(chore)
                .player(chore.getPlayer())
                .build();
        reviewRepository.save(review);
        chore.setHasBeenReviewed(true);
        choreService.save(chore);
        return review;
    }

    @Transactional
    public void leaveReview(int id, String comment,boolean completed) {
        Review review=reviewRepository.findById(id).orElseThrow(()->new RuntimeException("Review Not Found"));
        review.setComment(comment);
        review.setCompleted(completed);
        reviewRepository.save(review);
    }


    public Review findById(int id) {
        return reviewRepository.findById(id).orElseThrow(()->new RuntimeException("Review Not Found"));
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

}
