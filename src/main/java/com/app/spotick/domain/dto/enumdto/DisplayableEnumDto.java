package com.app.spotick.domain.dto.enumdto;

import com.app.spotick.domain.base.type.Displayable;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Data
public class DisplayableEnumDto {
    private String name;
    private String displayName;

    public DisplayableEnumDto(Displayable displayable) {
        this.name = displayable.name();
        this.displayName = displayable.getDisplayName();
    }

    public static List<DisplayableEnumDto> getDisplayableDtoList(Displayable[] enumArr) {
        return Arrays.stream(enumArr)
                .map(DisplayableEnumDto::new)
                .toList();
    }

}

