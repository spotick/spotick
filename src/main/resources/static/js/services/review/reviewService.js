export const reviewService = (() => {

    const registerReview = async (reservationId, score, content) => {
        const reviewRegisterDto = {
            reservationId: reservationId,
            score: score,
            content: content
        }

        const response = await fetch('/reviews/api/write', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(reviewRegisterDto)
        });

        return response.json();
    }

    const setNotReviewing = async (reservationId) => {
        const response = await fetch(`/reviews/api/notReviewing/${reservationId}`, {
            method: 'PATCH'
        });
        return response.json();
    }

    const editReview = async (reviewId, score, content) => {
        const placeReviewUpdateDto = {
            reviewId: reviewId,
            score: score,
            content: content
        }

        const response = await fetch('/reviews/api/edit', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(placeReviewUpdateDto)
        });

        return response.json();
    }

    return {
        registerReview: registerReview,
        setNotReviewing: setNotReviewing,
        editReview: editReview,
    }
})();