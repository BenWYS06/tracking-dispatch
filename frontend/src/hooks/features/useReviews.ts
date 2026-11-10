import { useCallback } from 'react'
import { useApi } from '.././common/useApi'
import * as reviewsApi from '../../api/reviews'
import type { ReviewResponse, ReviewReplyResponse, CreateReviewRequest, CreateReviewReplyRequest } from '../../types'
import type { Page } from '../../types/common'

export function useReviews() {
  const listApi = useApi<Page<ReviewResponse>>()
  const { execute: executeList } = listApi
  const createApi = useApi<ReviewResponse>()
  const { execute: executeCreate } = createApi
  const { execute: executeUpdate } = useApi<ReviewResponse>()
  const { execute: executeReply } = useApi<ReviewReplyResponse>()
  const { execute: executeUpdateReply } = useApi<ReviewReplyResponse>()

  const loadReviews = useCallback((productId: number, page = 0) => {
    return executeList(() => reviewsApi.getReviews(productId, page))
  }, [executeList])

  const createReview = useCallback((data: CreateReviewRequest) => {
    return executeCreate(() => reviewsApi.createReview(data))
  }, [executeCreate])

  const updateReview = useCallback((id: number, data: CreateReviewRequest) => {
    return executeUpdate(() => reviewsApi.updateReview(id, data))
  }, [executeUpdate])

  const addReply = useCallback((reviewId: number, data: CreateReviewReplyRequest) => {
    return executeReply(() => reviewsApi.addReply(reviewId, data))
  }, [executeReply])

  const updateReply = useCallback((replyId: number, data: CreateReviewReplyRequest) => {
    return executeUpdateReply(() => reviewsApi.updateReply(replyId, data))
  }, [executeUpdateReply])

  const deleteReply = useCallback((replyId: number) => {
    return reviewsApi.deleteReply(replyId)
  }, [])

  return {
    reviews: listApi.data?.content || [],
    totalPages: listApi.data?.page.totalPages || 0,
    loading: listApi.loading,
    error: listApi.error,
    loadReviews,
    createReview,
    updateReview,
    addReply,
    updateReply,
    deleteReply,
    createdReview: createApi.data
  }
}
