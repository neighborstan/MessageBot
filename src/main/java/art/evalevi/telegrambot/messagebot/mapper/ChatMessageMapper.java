package art.evalevi.telegrambot.messagebot.mapper;

import art.evalevi.telegrambot.messagebot.dto.ChatMessageDto;
import art.evalevi.telegrambot.messagebot.model.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for {@link ChatMessage} entity.
 */
@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    @Mapping(target = "id", ignore = true)
    ChatMessage messageDtoToMessage(ChatMessageDto messageDto);

    ChatMessageDto messageToMessageDto(ChatMessage message);
}
