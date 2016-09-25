from django.contrib import admin
from smile_api.models import FoodSource, WorkingHours


class BookInline(admin.TabularInline):
    model = WorkingHours
    extra = 1


class AuthorAdmin(admin.ModelAdmin):
    inlines = [
        BookInline,
    ]

admin.site.register(FoodSource, AuthorAdmin)
admin.site.register(WorkingHours)
