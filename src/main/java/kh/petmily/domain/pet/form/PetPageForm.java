package kh.petmily.domain.pet.form;

import kh.petmily.domain.find_board.form.FindBoardListForm;
import kh.petmily.domain.pet.Pet;
import lombok.Data;

import java.util.List;

@Data
public class PetPageForm {
    private int total;
    private int currentPage;
    private List<Pet> content;
    private int totalPages;
    private int startPage;
    private int endPage;

    public PetPageForm(int total, int currentPage, int size, List<Pet> content) {
        this.total = total;
        this.currentPage = currentPage;
        this.content = content;

        if (total == 0) {
            totalPages = 0;
            startPage = 0;
            endPage = 0;
        } else {
            totalPages = total / size;

            if (total % size > 0) {
                totalPages++;
            }

            int modVal = currentPage % 5;
            startPage = currentPage / 5 * 5 + 1;

            if (modVal == 0) startPage -= 5;

            endPage = startPage + 4;

            if (endPage > totalPages) endPage = totalPages;
        }
    }
}