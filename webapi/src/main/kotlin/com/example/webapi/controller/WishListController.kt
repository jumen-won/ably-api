package com.example.webapi.controller

import com.example.domain.base.common.PageQueryResult
import com.example.domain.entity.User
import com.example.domain.entity.WishList
import com.example.webapi.base.security.AuthenticatedUser
import com.example.webapi.dto.WishListInfo
import com.example.webapi.facade.WishListFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(WishListController.PATH_API_WISHLIST)
class WishListController(
    private val wishListFacade: WishListFacade
) {

    @GetMapping
    fun get(
        @AuthenticatedUser user: User,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<PageQueryResult<WishListInfo>> {
        return ResponseEntity.ok(wishListFacade.getMyWishList(user.id, page, size))
    }

    @PostMapping
    fun create(
        @AuthenticatedUser user: User,
        @RequestBody request: CreateWishListRequest
    ): ResponseEntity<WishList> {
        return ResponseEntity.ok(wishListFacade.create(user.id, request.title))
    }

    @DeleteMapping
    fun delete(
        @AuthenticatedUser user: User,
        @RequestBody request: DeleteWishListRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(wishListFacade.delete(user.id, request.wishListId))
    }

    companion object {
        private const val SERVICE_NAME = "wishlists"
        const val PATH_API_WISHLIST = "/$SERVICE_NAME"
    }

    data class CreateWishListRequest(
        val title: String
    )

    data class DeleteWishListRequest(
        val wishListId: Long
    )
}
