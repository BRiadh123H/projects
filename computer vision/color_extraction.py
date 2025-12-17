from colorthief import ColorThief
import matplotlib.pyplot as plt
import colorsys
import numpy as np

ct =ColorThief('colorful_photo.jpg')

dominant_color= ct.get_color(quality=1)
palette= ct.get_palette(color_count=6)
dominant_img = np.array([[dominant_color]], dtype=np.uint8)      # 1x1 block
palette_img = np.array([palette], dtype=np.uint8)                # 1x6 block

# Create subplots
fig, axs = plt.subplots(1, 2, figsize=(8, 4))

# Show dominant color
axs[0].imshow(dominant_img)
axs[0].set_title("Dominant Color")
axs[0].axis('off')

# Show palette
axs[1].imshow(palette_img)
axs[1].set_title("Palette")
axs[1].axis('off')

plt.show()

for color in palette:
    print("RGB:", color)
    print(f"#{color[0]:02x} {color[1]:02x} {color[2]:02x}")
    print("HSV:", colorsys.rgb_to_hsv(*color))
    print("HSL:", colorsys.rgb_to_hls(color[0], color[1], color[2]))