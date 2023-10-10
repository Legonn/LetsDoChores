package com.letschores.demo.controller;

import com.letschores.demo.model.Chore;
import com.letschores.demo.model.Review;
import com.letschores.demo.service.ChoreService;
import com.letschores.demo.service.PlayerService;
import com.letschores.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private ReviewService reviewService;
    private ChoreService choreService;
    private PlayerService playerService;


    @Autowired
    public ReviewController(ReviewService reviewService, ChoreService choreService, PlayerService playerService) {
        this.reviewService = reviewService;
        this.choreService = choreService;
        this.playerService = playerService;
    }

    @GetMapping("/createReview")
    public String createReview(@RequestParam("id") int choreId){
        Chore chore=choreService.findById(choreId);
        Review review=reviewService.createReview(chore);
        return "redirect:/review/findById?id=" +review.getId();
    }

    @PostMapping("/leaveReviewProcessor")
    public String leaveReview(@RequestParam("id") int id ,@RequestParam ("comment")
    String comment,@RequestParam("completed") boolean completed){
        reviewService.leaveReview(id,comment,completed);

        return "redirect:/player/gainCoins?reviewId="+id;
    }

    @GetMapping("/findById")
    public String findReviewById(@RequestParam ("id") int id,Model model){
        Review review=reviewService.findById(id);
        model.addAttribute("review",review);

        return "review";
    }

    @GetMapping("/findAllReviews")
    public String findAllReviews(@RequestParam ("playerId") int id,Model model){
        List<Review> reviews=playerService.findPlayerById(id).getReviews();

        reviews.sort(Comparator.comparing(Review::getLocalDate).reversed());
        model.addAttribute("reviews",reviews);

        return "display-reviews";
    }


}
