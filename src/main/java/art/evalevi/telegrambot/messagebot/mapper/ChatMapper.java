package art.evalevi.telegrambot.messagebot.mapper;

import art.evalevi.telegrambot.messagebot.dto.ChatDto;
import art.evalevi.telegrambot.messagebot.model.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * Mapper for {@link Chat} entity.
 */
@Mapper(componentModel = "spring")
public interface ChatMapper {

    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    Chat chatDtoToChat(ChatDto chatDto);

    ChatDto chatToChatDto(Chat chat);
}
