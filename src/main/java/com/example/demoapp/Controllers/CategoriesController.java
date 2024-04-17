package com.example.demoapp.Controllers;

import com.example.demoapp.Connectors.RestCategoryConnector;
import com.example.demoapp.Data.Category;
import com.example.demoapp.Data.Note;
import com.example.demoapp.Data.db.CategoriesEntity;
import com.example.demoapp.Data.db.UsersEntity;
import com.example.demoapp.Services.CategoriesService;
import com.example.demoapp.repositories.CategoriesRepository;
import com.example.demoapp.repositories.NotesRepository;
import com.example.demoapp.repositories.db.CategoriesEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Logged/Categories")
public class CategoriesController {

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    RestCategoryConnector restCategoryConnector;

    @Autowired
    CategoriesEntityRepository categoriesEntityRepository;

    @Autowired
    CategoriesRepository categoriesRepository;

    @Autowired
    private HttpSession session;
    @Autowired
    NotesRepository notesRepository;

    @GetMapping("/GetToDisplayingCategories")
    public String displayCategories(){
        return "redirect:/Logged/Categories/DisplayCategories";
    }

    @GetMapping("/DisplayCategories")
    public String getCategory(Model model){
       model.addAttribute("categories", categoriesService.getAllCategoriesDB());
       return "categories";
    }

    @GetMapping("/AddCategory")
    public String showNewCategoryForm(Model model){
        model.addAttribute("category", new Category());
        return "newCategory";
    }

    @PostMapping("/AddCategory")
    public String showNewCategoryForm(@Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            result.getAllErrors().forEach(el -> System.out.println(el));
            return "newCategory";
        }
        boolean categoryExistance = restCategoryConnector.checkCategoryExists(category.getCategoryName());

        if(!categoryExistance){
            result.rejectValue("categoryName", "err.string.RestCategoryName");
            result.getAllErrors().forEach(el -> System.out.println(el));
            return "newCategory";
        }
        if(categoriesService.findCategoryIdByName(category.getCategoryName()) != -1){
            result.rejectValue("categoryName", "err.string.NameAlreadyTaken");
            return "newCategory";
        }
        categoriesService.addNewCategory(category);
        return "redirect:/Logged/MainMenu";
    }

    @GetMapping("/GetToDeletingCategory/{categoryIndex}")
    public String deleteCategory(@PathVariable("categoryIndex") int categoryIndex, RedirectAttributes redirectAttributes){
        boolean deleteResult = categoriesService.deleteCategory(categoryIndex);
        if (deleteResult) {
            return "redirect:/Logged/Categories/DisplayCategories";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You can't delete category.");
            return "redirect:/Logged/Categories/DisplayCategories";
        }
    }



    @GetMapping("/GetToEditingCategory/{categoryIndex}")
    public String showeditCategory(@PathVariable("categoryIndex") int categoryIndex, Model model){
        CategoriesEntity categoriesEntity = categoriesService.findCategoryById(categoryIndex);
        model.addAttribute("category", categoriesEntity);
        return "editCategory";
    }

    @PostMapping("/EditingCategory")
    public String editCategory(@RequestParam("categoryId") Integer categoryId, @RequestParam("newCategoryName") String newCategoryName, RedirectAttributes redirectAttributes){
        UsersEntity usersEntity = (UsersEntity) session.getAttribute("UsersEntity");
        if(!restCategoryConnector.checkCategoryExists(newCategoryName)){

            redirectAttributes.addFlashAttribute("errorMessage", "The Category name must be a plural noun and start with lowercase letter.");
            String redirect = "redirect:/Logged/Categories/GetToEditingCategory/" + categoryId;
            return redirect;
        }

        if( categoriesService.findCategoryByNameFiltered(newCategoryName) == null || categoriesRepository.findCategoryByCategoryName(newCategoryName) == null ){
            if(categoriesService.findCategoryByNameFiltered(newCategoryName) == null && categoriesRepository.findCategoryByCategoryName(newCategoryName) == null && categoriesEntityRepository.findByCategoryId(categoryId) != null) {
                if(categoriesEntityRepository.findByCategoryId(categoryId) != null  && categoriesService.findCategoryIdByName(categoriesRepository.getDeletedCategoryList(),categoryId) == null){

                    CategoriesEntity categoriesEntity = categoriesService.copyCategoriesEntity(categoriesEntityRepository.findByCategoryId(categoryId));

                    List<CategoriesEntity> categoriesEntityList = categoriesRepository.getDeletedCategoryList();
                    categoriesEntity.setCategoryName(newCategoryName);
                    categoriesEntityList.add(categoriesEntity);
                    categoriesRepository.setDeletedCategoryList(categoriesEntityList);

                    categoriesEntity.setCategoryName(newCategoryName);
                    categoriesEntityList = categoriesRepository.getCategoryList();
                    categoriesEntityList.add(categoriesEntity);
                    categoriesRepository.setCategoryList(categoriesEntityList);

                    return "redirect:/Logged/Categories/DisplayCategories";
                }else {
                    List<CategoriesEntity> categoriesEntityList = categoriesService.setCategoryNameByDeletedCategoryId(categoryId,newCategoryName);
                    categoriesRepository.setDeletedCategoryList(categoriesEntityList);
                    return "redirect:/Logged/Categories/DisplayCategories";
                }

            }
            else if(categoriesRepository.findCategoryByCategoryName(newCategoryName) == null && categoriesService.findCategoryByNameFiltered(newCategoryName) == null && categoriesRepository.findCategoryById(categoriesRepository.getCategoryList(),categoryId) != null) {
                categoriesService.editCategoryRepo(categoryId,newCategoryName);

                return "redirect:/Logged/Categories/DisplayCategories";
            }
        }
        redirectAttributes.addFlashAttribute("errorMessage", "This name is already taken.");
        String redirect = "redirect:/Logged/Categories/GetToEditingCategory/" + categoryId;
        return redirect;
    }


}
