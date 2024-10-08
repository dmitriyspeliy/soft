package com.comfortablesoft.test.task.service;

import com.comfortablesoft.test.task.utils.Insert;
import com.comfortablesoft.test.task.utils.Sorted;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

import static org.apache.poi.ss.usermodel.CellType.BLANK;

@Service
@Slf4j
public class MainService {

    private Sheet sheet;
    private String path;

    public Integer findNum(String n, String path) {
        // если n = 0
        if (n == null || n.equals("") || n.equals("0")) throw new IllegalArgumentException("Invalid value n " + n);
        // путь к файлу
        this.path = path;
        // n -ое максимальное число
        int n1 = Integer.parseInt(n);
        // количество строк
        int count = 0;
        // отсортирован или нет
        boolean sort = false;
        // результирующий массив
        int[] mainArr = new int[n1];
        //  количество вставок в массив
        int countOfInsert = 0;

        try {
            // получаем первую страницу документа
            getSheet();

            // если нет данных
            if (sheet.getRow(0).getCell(0) == null) throw new IllegalArgumentException("Строк меньше, чем n");

            // луп по первому столбцу всех строк
            for (Row row : sheet) {
                // если нет ничего в ячейке - выход
                if (row.getCell(0).getCellType() == BLANK && row.getCell(0).getStringCellValue().equals("")) break;

                // берем значение ячейки
                int cell = (int) row.getCell(0).getNumericCellValue();

                // заполняем массив до n количества
                if (count < n1) {
                    mainArr[count] = cell;
                } else {
                    // сортируем только один раз
                    if (!sort) {
                        mainArr = Sorted.mergeSort(mainArr);
                        sort = true;
                    }

                    // если последнее число массива меньше, то нужно инсертнуть в конец.
                    // все числа сдвинуться и наше n максимальное число будет на позиции countOfInsert++
                    if (mainArr[mainArr.length - 1] < cell) {
                        mainArr = Insert.insertElement(mainArr, cell, mainArr.length);
                        countOfInsert++;
                    } else if (mainArr[0] < cell) {
                        // если первое число меньше, то нам нужно пройтись по всем числам
                        // как только будет больше, туда нужно инсертнуть
                        // все числа сдвинуться и наше n максимальное число будет на позиции countOfInsert++
                        for (int i = 0; i < mainArr.length; i++) {
                            if (mainArr[i] > cell) {
                                mainArr = Insert.insertElement(mainArr, cell, i);
                                countOfInsert++;
                                break;
                            }
                        }
                    }
                }
                count++;
            }
        } catch (IllegalArgumentException | IOException e) {
            throw new IllegalArgumentException(e);
        }


        // если строк меньше то нет смысла для всего этого
        if (count < n1) throw new IllegalArgumentException("Строк меньше, чем n");

        return mainArr[countOfInsert];
    }

    private void getSheet() throws IOException {
        FileInputStream file = new FileInputStream(path);
        Workbook workbook = new XSSFWorkbook(file);
        sheet = workbook.getSheetAt(0);
    }
}
