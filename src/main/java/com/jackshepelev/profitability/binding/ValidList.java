package com.jackshepelev.profitability.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidList<E> extends ArrayList<E> {
    @Valid
    private List<E> list;
}
