package com.example.webapi

import com.example.testbase.IntegrationTest
import com.example.webapi.base.exception.WishItemAlreadyExistsException
import com.example.webapi.base.exception.WishListNotFoundException
import com.example.webapi.facade.WishItemFacade
import com.example.webapi.facade.WishListFacade
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class WishItemIntegrationTest : IntegrationTest() {
    @Autowired
    private lateinit var wishListFacade: WishListFacade

    @Autowired
    private lateinit var wishItemFacade: WishItemFacade

    @Test
    fun `위시리스트 아이템 조회 성공 - 페이징`() {
        // given
        val userId = 1L
        val wishListTitle = "wishlist-1"
        val wishList = wishListFacade.create(userId, wishListTitle)

        repeat(5) { index ->
            wishItemFacade.addToWishList(userId, "product_$index", wishList.id)
        }

        // when
        val result = wishItemFacade.getByWishListId(wishList.id, page = 0, size = 3)

        // then
        assertEquals(3, result.content.size)
        assertEquals(5, result.totalElements)
        assertEquals(2, result.totalPages)
        assertTrue(result.hasNext)
    }

    @Test
    fun `아이템 추가 성공`() {
        // given
        val userId = 1L
        val wishListTitle = "wishlist-1"
        val wishList = wishListFacade.create(userId, wishListTitle)
        val itemCode = "product_0"

        // when & then
        assertDoesNotThrow {
            wishItemFacade.addToWishList(userId, itemCode, wishList.id)
        }

        // verify
        val result = wishItemFacade.getByWishListId(wishList.id, page = 0, size = 10)
        assertEquals(1, result.content.size)
        assertEquals(itemCode, result.content.first().itemInfo.code)
    }

    @Test
    fun `아이템 추가 실패 - 이미 존재하는 아이템`() {
        // given
        val userId = 1L
        val wishListTitle = "wishlist-1"
        val wishList = wishListFacade.create(userId, wishListTitle)
        val itemCode = "product_0"

        wishItemFacade.addToWishList(userId, itemCode, wishList.id)

        // when & then
        assertThrows<WishItemAlreadyExistsException> {
            wishItemFacade.addToWishList(userId, itemCode, wishList.id)
        }
    }

    @Test
    fun `아이템 추가 실패 - 존재하지 않는 위시리스트`() {
        // given
        val userId = 1L
        val nonExistentWishListId = 999L
        val itemCode = "product_0"

        // when & then
        assertThrows<WishListNotFoundException> {
            wishItemFacade.addToWishList(userId, itemCode, nonExistentWishListId)
        }
    }

    @Test
    fun `아이템 삭제 성공`() {
        // given
        val userId = 1L
        val wishListTitle = "wishlist-1"
        val wishList = wishListFacade.create(userId, wishListTitle)
        val itemCode = "product_0"

        wishItemFacade.addToWishList(userId, itemCode, wishList.id)

        // when
        wishItemFacade.delete(userId, itemCode)

        // then
        val result = wishItemFacade.getByWishListId(wishList.id, page = 0, size = 10)
        assertEquals(0, result.content.size)
    }
}
