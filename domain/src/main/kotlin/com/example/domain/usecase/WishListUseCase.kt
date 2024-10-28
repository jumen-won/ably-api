package com.example.domain.usecase

import com.example.domain.entity.WishList
import com.example.domain.repository.WishListRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WishListUseCase(
    private val repository: WishListRepository,
    private val wishItemUseCase: WishItemUseCase
) {

    fun create(command: Command.Create): WishList {
        val wishList = WishList(title = command.title, userId = command.userId)
        return repository.save(wishList)
    }

    fun delete(command: Command.Delete) {
        val (userId, wishListId) = command

        repository.deleteById(wishListId)
        wishItemUseCase.deleteAllByWishListId(WishItemUseCase.Command.DeleteAll(userId, wishListId))
    }

    sealed interface Command {
        data class Create(val title: String, val userId: Long) : Command
        data class Delete(val userId: Long, val wishListId: Long) : Command
    }
}
