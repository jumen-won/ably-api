package com.example.webapi

import com.example.testbase.IntegrationTest
import com.example.webapi.base.exception.WishListAlreadyExistsException
import com.example.webapi.facade.WishItemFacade
import com.example.webapi.facade.WishListFacade
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class WishListIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var wishListFacade: WishListFacade

    @Autowired
    private lateinit var wishItemFacade: WishItemFacade

    @Test
    fun `위시리스트 생성 성공`() {
        // given
        val userId = 1L
        val wishListTitle = "wishlist-1"

        // when & then
        assertDoesNotThrow {
            wishListFacade.create(userId, wishListTitle)
        }
    }

    @Test
    fun `위시리스트 생성 실패 - 이미 존재하는 제목`() {
        // given
        val userId = 1L
        val wishListTitle = "wishlist-1"
        wishListFacade.create(userId, wishListTitle)

        // when & then
        assertThrows<WishListAlreadyExistsException> {
            wishListFacade.create(userId, wishListTitle)
        }
    }

    @Test
    fun `위시리스트 조회 성공 - 페이징`() {
        // given
        val userId = 1L
        (1..5).forEach {
            wishListFacade.create(userId, "wishlist-$it")
        }

        // when
        val result = wishListFacade.getMyWishList(userId, page = 0, size = 3)

        // then
        assertEquals(3, result.content.size)
        assertEquals(5, result.totalElements)
        assertEquals(2, result.totalPages)
        assertTrue(result.hasNext)
    }
}
