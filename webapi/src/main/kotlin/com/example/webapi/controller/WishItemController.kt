package com.example.webapi.controller

import com.example.domain.base.common.PageQueryResult
import com.example.domain.entity.User
import com.example.webapi.base.security.AuthenticatedUser
import com.example.webapi.dto.WishItemInfo
import com.example.webapi.facade.WishItemFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(WishItemController.PATH_API_WISH_ITEM)
class WishItemController(
    private val wishItemFacade: WishItemFacade
) {

    @GetMapping
    fun get(
        @AuthenticatedUser user: User,
        @RequestParam wishListId: Long,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<PageQueryResult<WishItemInfo>> {
        return ResponseEntity.ok(wishItemFacade.getByWishListId(wishListId, page, size))
    }

    @PostMapping
    fun create(
        @AuthenticatedUser user: User,
        @RequestBody request: CreateWishItemRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(wishItemFacade.addToWishList(user.id, request.itemCode, request.wishListId))
    }

    @DeleteMapping
    fun delete(
        @AuthenticatedUser user: User,
        @RequestBody request: DeleteWishItemRequest
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(wishItemFacade.delete(user.id, request.itemCode))
    }

    companion object {
        private const val SERVICE_NAME = "wishitems"
        const val PATH_API_WISH_ITEM = "/$SERVICE_NAME"
    }

    data class CreateWishItemRequest(
        val itemCode: String,
        val wishListId: Long
    )

    data class DeleteWishItemRequest(
        val itemCode: String,
    )
}
