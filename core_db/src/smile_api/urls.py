from rest_framework import routers

from smile_api.views import FoodSourceVeiwSet

router = routers.DefaultRouter()
router.register(r'food_source', FoodSourceVeiwSet)
urlpatterns = router.urls
