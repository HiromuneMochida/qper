package com.example.qper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.qper.common.ConstantValue;
import com.example.qper.common.Pagination;
import com.example.qper.common.Util;
import com.example.qper.dto.SelectReviewDto;
import com.example.qper.entity.ReviewEntity;
import com.example.qper.form.ReviewForm;
import com.example.qper.service.OptionService;
import com.example.qper.service.ReviewService;

/**
 * レビュ―.
 */
@Controller
public class ReviewController {

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private OptionService optionService;

  /**
   * レビュ―一覧画面初期表示.
   *
   * @param dto
   * @param model
   * @return reviewList.html
   */
  @RequestMapping(value = "/review/getReviewList", method = RequestMethod.GET)
  public String getReviewList(SelectReviewDto dto, Model model) {

    //1ページの表示下限値
    dto.setLowerLimit(ConstantValue.PAGE_LIMIT_ZERO);
    //1ページの表示上限値
    dto.setUpperLimit(ConstantValue.PAGE_LIMIT);

    Pagination<SelectReviewDto> selectReview = new Pagination<SelectReviewDto>(reviewService.countReview(dto),
        ConstantValue.PAGE_LIMIT);

    selectReview.moveTo(ConstantValue.PAGE_START);
    //取得結果を格納する
    selectReview.setEntities(reviewService.selectReview(dto));

    model.addAttribute("selectReview", selectReview);

    model.addAttribute("authenticatedUser", Util.getUserEmail());

    return "reviewList";
  }

  /**
   * レビュ―一覧画面切替表示.
   *
   * @param dto
   * @param pageNo
   * @param model
   * @return reviewList.html
   */
  @RequestMapping(value = "/review/getReviewPageView", method = RequestMethod.GET)
  public String getReviewPageView(@RequestParam("pageNo") int pageNo, SelectReviewDto dto, Model model) {

    Pagination<SelectReviewDto> selectReview = new Pagination<SelectReviewDto>(reviewService.countReview(dto),
        ConstantValue.PAGE_LIMIT);
    //ページ移動
    selectReview.moveTo(pageNo);
    //1ページの表示下限値
    dto.setLowerLimit((pageNo - 1) * ConstantValue.PAGE_LIMIT);
    //1ページの表示上限値
    dto.setUpperLimit(ConstantValue.PAGE_LIMIT);
    //取得結果を格納する
    selectReview.setEntities(reviewService.selectReview(dto));

    model.addAttribute("selectReview", selectReview);

    model.addAttribute("authenticatedUser", Util.getUserEmail());

    return "reviewList";
  }

  /**
   * レビュ―登録画面初期表示.
   *
   * @param form
   * @param model
   * @return reviewRegist.html
   */
  @RequestMapping(value = "/review/getReviewRegist", method = RequestMethod.GET)
  public String getReviewRegist(ReviewForm form, Model model) {

    model.addAttribute("reviewForm", optionService.initializeReviewForm());

    return "reviewRegist";
  }

  /**
   * レビュ―登録処理.
   *
   * @param form
   * @param model
   * @return reviewList.html
   */
  @RequestMapping(value = "/review/postReviewRegist", method = RequestMethod.POST)
  public String postReviewRegist(ReviewForm form, Model model) {

    reviewService.insertReview(form);

    return "redirect:/review/getReviewList";
  }

  /**
   * レビュ―編集画面表示処理.
   *
   * @param form
   * @param model
   * @return reviewList.html
   */
  @RequestMapping(value = "/review/getReviewEdit/{postId}", method = RequestMethod.GET)
  public String getReviewEdit(@PathVariable("postId") int postId, ReviewForm form, ReviewEntity entity, Model model) {

    entity = reviewService.findReviewByPostId(postId);

    reviewService.entityToForm(entity, form);

    model.addAttribute("reviewEditForm", form);

    return "reviewEdit";
  }

  /**
   * レビュ―更新処理.
   *
   * @param form
   * @param model
   * @return reviewList.html
   */
  @RequestMapping(value = "/review/postReviewEdit/{postId}", method = RequestMethod.POST)
  public String postReviewEdit(@PathVariable("postId") int postId, ReviewForm form, ReviewEntity entity,
      Model model) {
    form.setPostId(postId);
    if (form.isDisp() == ConstantValue.PRIVATE_FLG_TRUE) {

      form.setPrivateFlg(ConstantValue.PRIVATE_FLG_ON);

    } else if (form.isDisp() == ConstantValue.PRIVATE_FLG_FALSE) {

      form.setPrivateFlg(ConstantValue.PRIVATE_FLG_OFF);

    }
    reviewService.updeteReview(form);

    return "redirect:/review/getReviewList";
  }

  /**
   * レビュ―削除画面表示.
   *
   * @param form
   * @param model
   * @return reviewDelete.html
   */
  @RequestMapping(value = "/review/getReviewDelete", method = RequestMethod.GET)
  public String getReviewDelete(ReviewForm form, SelectReviewDto dto, Model model) {

    //1ページの表示下限値
    dto.setLowerLimit(ConstantValue.PAGE_LIMIT_ZERO);
    //1ページの表示上限値
    dto.setUpperLimit(ConstantValue.PAGE_LIMIT);

    Pagination<SelectReviewDto> selectReview = new Pagination<SelectReviewDto>(reviewService.countReview(dto),
        ConstantValue.PAGE_LIMIT);

    selectReview.moveTo(ConstantValue.PAGE_START);
    //取得結果を格納する
    selectReview.setEntities(reviewService.selectReview(dto));

    model.addAttribute("selectReview", selectReview);

    model.addAttribute("form", new ReviewForm());

    model.addAttribute("authenticatedUser", Util.getUserEmail());

    return "reviewDelete";
  }

  /**
   * レビュ―削除処理.
   *
   * @param form
   * @param model
   * @return reviewList.html
   */
  @RequestMapping(value = "/review/postReviewDelete", method = RequestMethod.POST)
  public String postReviewDelete(@ModelAttribute("form") ReviewForm form, ReviewEntity entity, Model model) {

    reviewService.deleteReview(form);

    return "redirect:/review/getReviewList";
  }

  /**
   * レビュ―削除画面切替表示.
   *
   * @param pageNo
   * @param dto
   * @param form
   * @param model
   * @return reviewList.html
   */
  @RequestMapping(value = "/review/getReviewDeletePageView", method = RequestMethod.GET)
  public String getReviewDeletePageView(@RequestParam("pageNo") int pageNo, SelectReviewDto dto, ReviewForm form,
      Model model) {

    Pagination<SelectReviewDto> selectReview = new Pagination<SelectReviewDto>(reviewService.countReview(dto),
        ConstantValue.PAGE_LIMIT);
    //ページ移動
    selectReview.moveTo(pageNo);

    //1ページの表示下限値
    dto.setLowerLimit((pageNo - 1) * ConstantValue.PAGE_LIMIT);
    //1ページの表示上限値
    dto.setUpperLimit(ConstantValue.PAGE_LIMIT);
    //取得結果を格納する
    selectReview.setEntities(reviewService.selectReview(dto));

    model.addAttribute("selectReview", selectReview);

    model.addAttribute("authenticatedUser", Util.getUserEmail());

    model.addAttribute("form", form);

    return "reviewDelete";
  }

}
