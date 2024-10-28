package com.example.domain.usecase

import com.example.domain.base.exception.ItemNotFoundDomainException
import com.example.domain.entity.WishItem
import com.example.domain.repository.ItemRepository
import com.example.domain.repository.WishItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WishItemUseCase(
    private val repository: WishItemRepository,
    private val itemRepository: ItemRepository
) {

    fun create(command: Command.Create): WishItem {
        val (itemCode, wishListId, userId) = command
        val item = itemRepository.findByCode(itemCode) ?: throw ItemNotFoundDomainException()
        return repository.save(
            WishItem(
                item = item,
                wishListId = wishListId,
                userId = userId
            )
        )
    }

    fun delete(command: Command.Delete) {
        val (userId, itemCode) = command
        repository.deleteByUserIdAndItemCode(userId, itemCode)
    }

    fun deleteAllByWishListId(command: Command.DeleteAll) {
        val (userId, wishListId) = command
        repository.deleteAllByUserIdAndWishListId(userId, wishListId)
    }

    sealed interface Command {
        data class Create(val itemCode: String, val wishListId: Long, val userId: Long) : Command
        data class Delete(val userId: Long, val itemCode: String) : Command
        data class DeleteAll(val userId: Long, val wishListId: Long) : Command
    }
}
