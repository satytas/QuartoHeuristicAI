package GraphicsFramework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import logicComponents.Piece;

public class pieceButton extends JButton {
    private static final long serialVersionUID = 1L;

    private static final String texurePath = "/assets/textures/pieces/";
    private static final ImageIcon bgIcon = new ImageIcon(
            pieceButton.class.getResource(texurePath + "emptyPlace.png")
    );

    // Properties:
    private Point location;
    private ImageIcon texure;

    // Constructors:
    public pieceButton(Piece piece) {
        this.texure = new ImageIcon(
                pieceButton.class.getResource(texurePath + piece.getName() + ".png")
        );
        setIconBoth();
        initialize();

        location = piece.getPlaceOnBoard();
    }

    public pieceButton(int row, int col) {
        setIcon(bgIcon);
        initialize();

        location = new Point(row, col);
    }

    public pieceButton() {
        setIcon(bgIcon);
        setBorder(BorderFactory.createEmptyBorder());
    }

    // Other Functions:
    public void setIconBg() {
        setIcon(bgIcon);
    }

    public void setIconPiece() {
        if (texure != null) {
            BufferedImage iconImg = new BufferedImage(
                    texure.getIconWidth(), texure.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = iconImg.createGraphics();

            int width = (int) (texure.getIconWidth() * 0.7);
            int height = (int) (texure.getIconHeight() * 0.7);

            int overlayX = (texure.getIconWidth() - width) / 10;
            int overlayY = (int) ((texure.getIconHeight() - height) / 2.5);

            g.drawImage(texure.getImage(), overlayX, overlayY, width, height, null);

            g.dispose();

            ImageIcon combinedIcon = new ImageIcon(iconImg);

            setIcon(combinedIcon);

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }
    }

    public void setIconBoth() {
        if (texure != null) {
            BufferedImage combinedImg = new BufferedImage(
                    bgIcon.getIconWidth(), bgIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = combinedImg.createGraphics();

            g.drawImage(bgIcon.getImage(), 0, 0, null);

            int x = (bgIcon.getIconWidth() - texure.getIconWidth()) / 2;
            int y = (bgIcon.getIconHeight() - texure.getIconHeight()) / 2;

            int width = (int) (texure.getIconWidth() * 0.7);
            int height = (int) (texure.getIconHeight() * 0.7);

            int overlayX = x + (bgIcon.getIconWidth() - width) / 12;
            int overlayY = y + (int) ((bgIcon.getIconHeight() - height) / 2.5);

            g.drawImage(texure.getImage(), overlayX, overlayY, width, height, null);

            g.dispose();

            ImageIcon combinedIcon = new ImageIcon(combinedImg);

            setIcon(combinedIcon);
        }
    }

    public void updatePieceIcon(ImageIcon newPieceIcon) {
        this.texure = newPieceIcon;
    }

    private void initialize() {
        setBorder(BorderFactory.createEmptyBorder());

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.lightGray, 3));
            }

            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createEmptyBorder());
            }
        });
    }

    // Getters & Setters:
    public ImageIcon getTexure() {
        return texure;
    }

    public Point getLocation() {
        return location;
    }
}
